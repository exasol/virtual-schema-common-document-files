package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.documentfetcher.files.ColumnSizeCalculator.*;

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
import io.deephaven.csv.tokenization.Tokenizer.CustomTimeZoneParser;
import io.deephaven.csv.util.*;

/**
 * {@link SingleFileSchemaFetcher} for CSV files.
 */
public class CsvSchemaFetcher implements SingleFileSchemaFetcher {
    private static final Logger LOG = Logger.getLogger(CsvSchemaFetcher.class.getName());
    private static final long MAX_ROW_COUNT = 10_000;

    @Override
    public MappingDefinition fetchSchema(final RemoteFile remoteFile) {
        // Hard coded to false for now, will be detected automatically in
        // https://github.com/exasol/virtual-schema-common-document-files/issues/131
        final boolean hasHeaderRow = false;
        final CsvReader.Result result = parseCsv(remoteFile, hasHeaderRow);
        return new DefinitionBuilder(result, hasHeaderRow).build();
    }

    private CsvReader.Result parseCsv(final RemoteFile remoteFile, final boolean hasHeaderRow) {
        try (InputStream inputStream = remoteFile.getContent().getInputStream()) {
            return CsvReader.read(csvParserConfiguration(hasHeaderRow), inputStream, new NullSinkFactory());
        } catch (final IOException | CsvReaderException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-70")
                            .message("Failed to read CSV file {{file path}}", remoteFile.getResourceName()).toString(),
                    exception);
        }
    }

    private CsvSpecs csvParserConfiguration(final boolean hasHeaderRow) {
        return CsvSpecs.builder() //
                .hasHeaderRow(hasHeaderRow) //
                .parsers(Parsers.DEFAULT) //
                .customTimeZoneParser(new EmptyTimeZoneParser()) //
                .numRows(MAX_ROW_COUNT) //
                .build();
    }

    private static class DefinitionBuilder {
        private final Fields.FieldsBuilder fields = Fields.builder();
        private final CsvReader.Result csvResult;
        private final boolean hasHeaderRow;

        DefinitionBuilder(final CsvReader.Result csvResult, final boolean hasHeaderRow) {
            this.csvResult = csvResult;
            this.hasHeaderRow = hasHeaderRow;
        }

        MappingDefinition build() {
            int columnIndex = 0;
            LOG.finest(() -> "Building definition for CSV " + (hasHeaderRow ? "with" : "without") + " header, "
                    + csvResult.numCols() + " columns and " + csvResult.numRows() + " rows");
            for (final CsvReader.ResultColumn column : this.csvResult) {
                addField(column, columnIndex);
                columnIndex++;
            }
            return this.fields.build();
        }

        private void addField(final CsvReader.ResultColumn column, final int columnIndex) {
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
                return ToVarcharMapping.builder().varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE);
            case CHAR:
                return ToVarcharMapping.builder().varcharColumnSize(1);
            case BOOLEAN_AS_BYTE:
                return ToBoolMapping.builder();
            case BYTE:
            case SHORT:
            case INT:
                return ToDecimalMapping.builder().decimalPrecision(INT_32_DIGITS).decimalScale(0);
            case LONG:
                return ToDecimalMapping.builder().decimalPrecision(INT_64_DIGITS).decimalScale(0);
            case FLOAT:
            case DOUBLE:
                return ToDoubleMapping.builder();
            case DATETIME_AS_LONG:
            case TIMESTAMP_AS_LONG:
                // Not supported
                return ToVarcharMapping.builder();
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
