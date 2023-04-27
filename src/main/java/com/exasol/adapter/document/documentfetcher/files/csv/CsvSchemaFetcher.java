package com.exasol.adapter.document.documentfetcher.files.csv;

import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.ToUpperSnakeCaseConverter;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;

import io.deephaven.csv.reading.CsvReader;

/**
 * {@link SingleFileSchemaFetcher} for CSV files.
 */
public class CsvSchemaFetcher implements SingleFileSchemaFetcher {
    private static final Logger LOG = Logger.getLogger(CsvSchemaFetcher.class.getName());
    private static final long MAX_ROW_COUNT = 10_000;

    @Override
    public InferredMappingDefinition fetchSchema(final RemoteFile remoteFile) {
        final boolean hasHeaderRow = new CsvHeaderDetector(remoteFile).hasHeaderRow();
        final CsvReader.Result result = CsvParser.parse(remoteFile, hasHeaderRow, MAX_ROW_COUNT);
        return new MappingDefinitionBuilder(result, hasHeaderRow).build();
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
            LOG.finest(() -> "Building definition for CSV " + (this.hasHeaderRow ? "with" : "without") + " header, "
                    + this.csvResult.numCols() + " columns and " + this.csvResult.numRows() + " rows");
            for (final CsvReader.ResultColumn column : this.csvResult) {
                addColumn(column, columnIndex++);
            }
            return InferredMappingDefinition.builder(this.fields.build())
                    .additionalConfiguration(getAdditionalConfiguration()).build();
        }

        private String getAdditionalConfiguration() {
            return "{\"csv-headers\": " + this.hasHeaderRow + "}";
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
}
