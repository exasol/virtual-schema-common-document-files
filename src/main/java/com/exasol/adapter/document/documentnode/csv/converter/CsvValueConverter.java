package com.exasol.adapter.document.documentnode.csv.converter;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * Classes implementing this interface convert a value from a CSV file from {@link String} to a sub-type of
 * {@link DocumentNode}.
 */
@FunctionalInterface
public interface CsvValueConverter {
    /**
     * Convert the given value from a CSV file to a sub-type of {@link DocumentNode}.
     * 
     * @param value value from a CSV file
     * @return the converted value
     */
    DocumentNode convert(String value);
}