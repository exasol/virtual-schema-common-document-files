package com.exasol.adapter.document.documentnode.csv.converter;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.*;
import com.exasol.adapter.document.documentpath.ObjectLookupPathSegment;
import com.exasol.adapter.document.documentpath.PathSegment;
import com.exasol.adapter.document.mapping.*;
import com.exasol.errorreporting.ExaError;

/**
 * This registry allows finding the correct {@link ValueConverter} for a given column in a CSV file -- either by index
 * (for CSV files without header) or by column name (for CSV files with header).
 */
public class CsvValueTypeConverterRegistry {

    private final Map<String, ValueConverter> convertersByColumnName;
    private final Map<Integer, ValueConverter> convertersByColumnIndex;

    private CsvValueTypeConverterRegistry(final Map<String, ValueConverter> convertersByColumnName,
            final Map<Integer, ValueConverter> convertersByColumnIndex) {
        this.convertersByColumnName = convertersByColumnName;
        this.convertersByColumnIndex = convertersByColumnIndex;
    }

    /**
     * Create a new registry with {@link ValueConverter}s for the given CSV columns.
     * 
     * @param csvColumns the column types of the CSV file
     * @return a new registry configured for the given columns
     */
    public static CsvValueTypeConverterRegistry create(final List<ColumnMapping> csvColumns) {
        return new Builder(csvColumns).build();
    }

    /**
     * Get the {@link ValueConverter} for the CSV column with the given index. Use this method for CSV files without
     * headers.
     * 
     * @param columnIndex the zero-based index of the CSV column
     * @return the {@link ValueConverter} for the given column
     */
    public ValueConverter findConverter(final int columnIndex) {
        final ValueConverter converter = this.convertersByColumnIndex.get(columnIndex);
        if (converter == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-60").message(
                    "No CSV value converter found for column {{column index}}. Available converters: {{available converters}}.",
                    columnIndex, this.convertersByColumnIndex).ticketMitigation().toString());
        }
        return converter;
    }

    /**
     * Get the {@link ValueConverter} for the CSV column with the given column name. Use this method for CSV files with
     * headers.
     * 
     * @param columnIndex the name of the CSV column
     * @return the {@link ValueConverter} for the given column
     */
    public ValueConverter findConverter(final String columnName) {
        final ValueConverter converter = this.convertersByColumnName.get(columnName);
        if (converter == null) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-61").message(
                    "No CSV value converter found for column {{column name}}. Available converters: {{available converters}}.",
                    columnName, this.convertersByColumnName).ticketMitigation().toString());
        }
        return converter;
    }

    private static class Builder {
        private final List<ColumnMapping> csvColumns;

        private Builder(final List<ColumnMapping> csvColumns) {
            this.csvColumns = Objects.requireNonNull(csvColumns, "csvColumns");
        }

        private CsvValueTypeConverterRegistry build() {
            final List<ColumnMapping> filteredColumns = this.csvColumns.stream()
                    .filter(col -> !(col instanceof SourceReferenceColumnMapping)).collect(toList());
            final Map<String, ValueConverter> convertersByColumnName = new HashMap<>();
            final Map<Integer, ValueConverter> convertersByColumnIndex = new HashMap<>();
            int index = 0;
            for (final ColumnMapping columnMapping : filteredColumns) {
                final ValueConverter converter = createConverter(columnMapping);
                convertersByColumnIndex.put(index, converter);
                convertersByColumnName.put(getColumnName(columnMapping), converter);
                index++;
            }
            return new CsvValueTypeConverterRegistry(convertersByColumnName, convertersByColumnIndex);
        }

        private String getColumnName(final ColumnMapping columnMapping) {
            if (!(columnMapping instanceof PropertyToColumnMapping)) {
                throw unsupportedMappingException(columnMapping);
            }
            return getColumnNameFromPath((PropertyToColumnMapping) columnMapping);
        }

        private IllegalArgumentException unsupportedMappingException(final ColumnMapping columnMapping) {
            return new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-62")
                    .message("Column mapping of type {{type}} (value: {{mapping}}) is not supported.",
                            columnMapping.getClass().getName(), columnMapping)
                    .mitigation("Please use only supported column mappings.").toString());
        }

        private String getColumnNameFromPath(final PropertyToColumnMapping propertyMapping) {
            if (propertyMapping.getPathToSourceProperty().getSegments().size() != 1) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-63")
                        .message("Path to source {{path}} has {{segment count}} segments.",
                                propertyMapping.getPathToSourceProperty(),
                                propertyMapping.getPathToSourceProperty().size())
                        .mitigation("Please specify a path with exactly one segment.").toString());
            }
            final PathSegment segment = propertyMapping.getPathToSourceProperty().getSegments().get(0);
            if (!(segment instanceof ObjectLookupPathSegment)) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-64")
                        .message("Segment {{segment}} of path {{path}} has an unsupported type {{segment type}}.",
                                segment, propertyMapping.getPathToSourceProperty(), segment.getClass().getName())
                        .mitigation("Please use only supported path segment of type {{supported segment type}}.",
                                ObjectLookupPathSegment.class.getName())
                        .toString());
            }
            return ((ObjectLookupPathSegment) segment).getLookupKey();
        }

        private ValueConverter createConverter(final ColumnMapping columnMapping) {
            if (columnMapping instanceof PropertyToVarcharColumnMapping) {
                return StringHolderNode::new;
            } else if (columnMapping instanceof PropertyToBoolColumnMapping) {
                return Builder::booleanConverter;
            } else if (columnMapping instanceof PropertyToDecimalColumnMapping) {
                return Builder::decimalConverter;
            } else if (columnMapping instanceof PropertyToDoubleColumnMapping) {
                return Builder::doubleConverter;
            } else if (columnMapping instanceof PropertyToDateColumnMapping) {
                return Builder::dateConverter;
            } else if (columnMapping instanceof PropertyToTimestampColumnMapping) {
                return Builder::timestampConverter;
            }
            throw unsupportedMappingException(columnMapping);
        }

        private static DocumentNode booleanConverter(final String orgValue) {
            final String value = orgValue.toLowerCase();
            if (value.equals("true")) {
                return new BooleanHolderNode(true);
            }
            if (value.equals("false")) {
                return new BooleanHolderNode(false);
            }
            throw new IllegalArgumentException(
                    ExaError.messageBuilder("E-VSDF-65").message("Value {{value}} is not a boolean value.", orgValue)
                            .mitigation("Please use only values 'true' and 'false' (case insensitive).").toString());
        }

        private static DocumentNode decimalConverter(final String value) {
            return new BigDecimalHolderNode(new BigDecimal(value));
        }

        private static DocumentNode doubleConverter(final String value) {
            return new DoubleHolderNode(Double.parseDouble(value));
        }

        private static DocumentNode dateConverter(final String value) {
            return new DateHolderNode(java.sql.Date.valueOf(value));
        }

        private static DocumentNode timestampConverter(final String value) {
            return new TimestampHolderNode(java.sql.Timestamp.valueOf(value));
        }
    }
}
