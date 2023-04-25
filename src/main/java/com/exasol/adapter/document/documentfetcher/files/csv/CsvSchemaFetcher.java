package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.ToUpperSnakeCaseConverter;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.errorreporting.ExaError;

import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.parsers.DataType;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.reading.CsvReader.Result;
import io.deephaven.csv.reading.CsvReader.ResultColumn;
import io.deephaven.csv.sinks.SinkFactory;
import io.deephaven.csv.util.CsvReaderException;

public class CsvSchemaFetcher implements SingleFileSchemaFetcher {
    private static final Logger LOG = Logger.getLogger(CsvSchemaFetcher.class.getName());
    private static final long MAX_ROW_COUNT = 10_000;

    @Override
    public MappingDefinition fetchSchema(final RemoteFile remoteFile) {
        final Result result = parseCsv(remoteFile);
        return new DefinitionBuilder(result).build();
    }

    private Result parseCsv(final RemoteFile remoteFile) {
        final CsvSpecs specs = CsvSpecs.builder().numRows(MAX_ROW_COUNT).build();
        try (InputStream inputStream = remoteFile.getContent().getInputStream()) {
            return CsvReader.read(specs, inputStream, SinkFactory.arrays());
        } catch (final IOException | CsvReaderException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-70").message("").toString(), exception);
        }
    }

    private static class DefinitionBuilder {
        private final Fields.FieldsBuilder fields = Fields.builder();
        private final Result csvResult;

        DefinitionBuilder(final Result csvResult) {
            this.csvResult = csvResult;
        }

        MappingDefinition build() {
            int columnIndex = 0;
            for (final ResultColumn column : this.csvResult) {
                addField(column, columnIndex);
                columnIndex++;
            }
            return this.fields.build();
        }

        private void addField(final ResultColumn column, final int columnIndex) {
            final MappingDefinition mapping = getMapping(column.dataType(), getColumnName(column.name(), columnIndex));
            LOG.finest(() -> "Mapping CSV column #" + columnIndex + " '" + column.name() + "' of type "
                    + column.dataType() + " to " + mapping);
            this.fields.mapField(getFieldName(column.name(), columnIndex), mapping);
        }

        private String getColumnName(final String fieldName, final int columnIndex) {
            if (fieldName != null) {
                return ToUpperSnakeCaseConverter.toUpperSnakeCase(fieldName);
            }
            return "COLUMN_" + columnIndex;
        }

        private MappingDefinition getMapping(final DataType dataType, final String columnName) {
            return ToVarcharMapping.builder().destinationName(columnName).build();
        }

        private String getFieldName(final String fieldName, final int columnIndex) {
            if (fieldName != null) {
                return fieldName;
            }
            return String.valueOf(columnIndex);
        }
    }
}
