package com.exasol.adapter.document.documentnode.csv;

import java.util.List;

import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.mapping.ColumnMapping;

import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This is a factory that creates {@link DocumentObject}s for {@link NamedCsvRow named} and {@link CsvRow regular} CSV
 * rows.
 */
public class CsvObjectNodeFactory {

    private final CsvValueTypeConverter typeConverter;

    /**
     * Create a new {@link CsvObjectNodeFactory}.
     * 
     * @param csvColumns the CSV column types
     * @return a new {@link CsvObjectNodeFactory}
     */
    public static CsvObjectNodeFactory create(final List<ColumnMapping> csvColumns) {
        return new CsvObjectNodeFactory(new CsvValueTypeConverter(csvColumns));
    }

    CsvObjectNodeFactory(final CsvValueTypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    /**
     * Create a new {@link DocumentObject} for the given {@link NamedCsvRow}.
     * 
     * @param namedCsvRow the row to convert
     * @return a new {@link DocumentObject}
     */
    public DocumentObject create(final NamedCsvRow namedCsvRow) {
        return new NamedCsvObjectNode(typeConverter, namedCsvRow);
    }

    /**
     * Create a new {@link DocumentObject} for the given {@link CsvRow}.
     * 
     * @param csvRow the row to convert
     * @return a new {@link DocumentObject}
     */
    public DocumentObject create(final CsvRow csvRow) {
        return new CsvObjectNode(typeConverter, csvRow);
    }
}
