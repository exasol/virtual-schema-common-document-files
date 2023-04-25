package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.ToUpperSnakeCaseConverter;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.AbstractToColumnMapping.AbstractToColumnMappingBuilder;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.errorreporting.ExaError;

import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.containers.ByteSlice;
import io.deephaven.csv.parsers.DataType;
import io.deephaven.csv.parsers.Parsers;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.reading.CsvReader.Result;
import io.deephaven.csv.reading.CsvReader.ResultColumn;
import io.deephaven.csv.tokenization.Tokenizer.CustomTimeZoneParser;
import io.deephaven.csv.util.*;

public class CsvSchemaFetcher implements SingleFileSchemaFetcher {
    private static final Logger LOG = Logger.getLogger(CsvSchemaFetcher.class.getName());
    private static final long MAX_ROW_COUNT = 10_000;

    @Override
    public MappingDefinition fetchSchema(final RemoteFile remoteFile) {
        final boolean hasHeaderRow = false;
        final Result result = parseCsv(remoteFile, hasHeaderRow);
        return new DefinitionBuilder(result, hasHeaderRow).build();
    }

    private Result parseCsv(final RemoteFile remoteFile, final boolean hasHeaderRow) {
        final CsvSpecs specs = CsvSpecs.builder().hasHeaderRow(hasHeaderRow).parsers(Parsers.DEFAULT)
                .customTimeZoneParser(new EmptyTimeZoneParser()).numRows(MAX_ROW_COUNT).build();
        try (InputStream inputStream = remoteFile.getContent().getInputStream()) {
            return CsvReader.read(specs, inputStream, new NullSinkFactory());
        } catch (final IOException | CsvReaderException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-70").message("").toString(), exception);
        }
    }

    private static class DefinitionBuilder {
        private final Fields.FieldsBuilder fields = Fields.builder();
        private final Result csvResult;
        private final boolean hasHeaderRow;

        DefinitionBuilder(final Result csvResult, final boolean hasHeaderRow) {
            this.csvResult = csvResult;
            this.hasHeaderRow = hasHeaderRow;
        }

        MappingDefinition build() {
            int columnIndex = 0;
            LOG.finest(() -> "Building definition for CSV " + (hasHeaderRow ? "with" : "without") + " header, "
                    + csvResult.numCols() + " columns and " + csvResult.numRows() + " rows");
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
            if (hasHeaderRow) {
                return ToUpperSnakeCaseConverter.toUpperSnakeCase(fieldName);
            }
            return "COLUMN_" + columnIndex;
        }

        private MappingDefinition getMapping(final DataType dataType, final String columnName) {
            return createBuilder(dataType).destinationName(columnName).build();
        }

        private AbstractToColumnMappingBuilder<?, ?> createBuilder(final DataType dataType) {
            switch (dataType) {
            case STRING:
                return ToVarcharMapping.builder();
            case CHAR:
                return ToVarcharMapping.builder().varcharColumnSize(1);
            case BOOLEAN_AS_BYTE:
                return ToBoolMapping.builder();
            case BYTE:
            case INT:
                return ToDecimalMapping.builder().decimalPrecision(10).decimalScale(0);
            case LONG:
                return ToDecimalMapping.builder().decimalPrecision(19).decimalScale(0);
            case FLOAT:
            case DOUBLE:
                return ToDecimalMapping.builder().decimalPrecision(36).decimalScale(10);
            case DATETIME_AS_LONG:
                return ToTimestampMapping.builder();
            default:
                throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-71")
                        .message("Unknown data type {{data type}}.", dataType).ticketMitigation().toString());
            }
        }

        private String getFieldName(final String fieldName, final int columnIndex) {
            if (hasHeaderRow) {
                return fieldName;
            }
            return String.valueOf(columnIndex);
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
