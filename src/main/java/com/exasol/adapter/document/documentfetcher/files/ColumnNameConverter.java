package com.exasol.adapter.document.documentfetcher.files;

/**
 * This interface allows customizing the mapping of column names when using automatic schema inference. It converts
 * column names from the source (e.g. Parquet, CSV) to Exasol column names.
 */
@FunctionalInterface
public interface ColumnNameConverter {
    /**
     * Convert a column name from the source to an Exasol column name.
     * 
     * @param columnName source column name
     * @return Exasol column name
     */
    String convertColumnName(String columnName);

    /**
     * Create a column name converter that converts column names to {@code UPPER_SNAKE_CASE}.
     * 
     * @return column name converter
     */
    public static ColumnNameConverter upperSnakeCaseConverter() {
        return ToUpperSnakeCaseConverter::toUpperSnakeCase;
    }

    /**
     * Create a column name converter that does not change the column names.
     * 
     * @return column name converter
     */
    public static ColumnNameConverter originalNameConverter() {
        return name -> name;
    }
}
