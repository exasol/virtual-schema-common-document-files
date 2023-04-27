package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.ToUpperSnakeCaseConverter;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;
import com.exasol.errorreporting.ExaError;

import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.containers.ByteSlice;
import io.deephaven.csv.parsers.Parsers;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.tokenization.Tokenizer.CustomTimeZoneParser;
import io.deephaven.csv.util.*;

/**
 * {@link SingleFileSchemaFetcher} for CSV files.
 */
public class CsvSchemaFetcher implements SingleFileSchemaFetcher {
    private static final Logger LOG = Logger.getLogger(CsvSchemaFetcher.class.getName());
    private static final long MAX_ROW_COUNT = 10_000;

    @Override
    public InferredMappingDefinition fetchSchema(final RemoteFile remoteFile) {
        // Hard coded to false for now, will be detected automatically in
        // https://github.com/exasol/virtual-schema-common-document-files/issues/131
        final boolean hasHeaderRow = false;
        final CsvReader.Result result = analyzeCsv(remoteFile, hasHeaderRow, MAX_ROW_COUNT);
        return new MappingDefinitionBuilder(result, hasHeaderRow).build();
    }

    private CsvReader.Result analyzeCsv(final RemoteFile remoteFile, final boolean hasHeaderRow, final long lookAhead) {
        final CsvSpecs parserConfiguration = csvParserConfiguration(hasHeaderRow, lookAhead);
        try (InputStream inputStream = remoteFile.getContent().getInputStream()) {
            return CsvReader.read(parserConfiguration, inputStream, new NullSinkFactory());
        } catch (final IOException | CsvReaderException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-70")
                            .message("Failed to read CSV file {{file path}}", remoteFile.getResourceName()).toString(),
                    exception);
        }
    }

    private CsvSpecs csvParserConfiguration(final boolean hasHeaderRow, final long lookAhead) {
        return CsvSpecs.builder() //
                .hasHeaderRow(hasHeaderRow) //
                .parsers(Parsers.DEFAULT) //
                .customTimeZoneParser(new EmptyTimeZoneParser()) //
                .numRows(lookAhead) //
                .build();
    }

    private static class MappingDefinitionBuilder {
        private final Fields.FieldsBuilder fields = Fields.builder();
        private final CsvReader.Result csvResult;
        private final boolean hasHeaderRow;

        MappingDefinitionBuilder(final CsvReader.Result csvResult, final boolean hasHeaderRow) {
            this.csvResult = csvResult;
            this.hasHeaderRow = hasHeaderRow;
        }

        InferredMappingDefinition build() {
            int columnIndex = 0;
            LOG.finest(() -> "Building definition for CSV " + (hasHeaderRow ? "with" : "without") + " header, "
                    + csvResult.numCols() + " columns and " + csvResult.numRows() + " rows");
            for (final CsvReader.ResultColumn column : this.csvResult) {
                addColumn(column, columnIndex++);
            }
            return InferredMappingDefinition.builder(this.fields.build()).build();
        }

        private void addColumn(final CsvReader.ResultColumn column, final int columnIndex) {
            final ColumnMapper colMapper = createColumnMapper(column, columnIndex);
            final MappingDefinition mapping = colMapper.getMapping();
            LOG.finest(() -> "Mapping CSV column #" + columnIndex + " '" + column.name() + "' of type "
                    + column.dataType() + " to " + mapping);
            this.fields.mapField(colMapper.getSource(), mapping);
        }

        ColumnMapper createColumnMapper(final CsvReader.ResultColumn column, final int columnIndex) {
            if (this.hasHeaderRow) {
                final String csvName = column.name();
                return new ColumnMapper(csvName, ToUpperSnakeCaseConverter.toUpperSnakeCase(csvName),
                        column.dataType());
            } else {
                return new ColumnMapper(String.valueOf(columnIndex), "COLUMN_" + columnIndex, column.dataType());
            }
        }
    }

    private static class EmptyTimeZoneParser implements CustomTimeZoneParser {
        @Override
        public boolean tryParse(final ByteSlice bs, final MutableObject<ZoneId> zoneId,
                final MutableLong offsetSeconds) {
            if (bs.size() == 0) {
                zoneId.setValue(ZoneId.of("UTC"));
                offsetSeconds.setValue(0);
                return true;
            }
            return false;
        }
    }
}
