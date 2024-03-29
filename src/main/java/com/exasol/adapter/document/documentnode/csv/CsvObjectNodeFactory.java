package com.exasol.adapter.document.documentnode.csv;

import java.util.List;

import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueConverters;
import com.exasol.adapter.document.mapping.ColumnMapping;

import de.siegmar.fastcsv.reader.CsvRecord;
import de.siegmar.fastcsv.reader.NamedCsvRecord;

/**
 * This is a factory that creates {@link DocumentObject}s for {@link NamedCsvRecord named} and {@link CsvRecord regular}
 * CSV rows.
 */
public class CsvObjectNodeFactory {

    private final CsvValueConverters converters;
    private final String resourceName;

    /**
     * Create a new {@link CsvObjectNodeFactory}.
     *
     * @param resourceName the resource name or file path of the CSV file
     * @param csvColumns   the CSV column types
     * @return a new {@link CsvObjectNodeFactory}
     */
    public static CsvObjectNodeFactory create(final String resourceName, final List<ColumnMapping> csvColumns) {
        return new CsvObjectNodeFactory(resourceName, CsvValueConverters.create(csvColumns));
    }

    private CsvObjectNodeFactory(final String resourceName, final CsvValueConverters converters) {
        this.resourceName = resourceName;
        this.converters = converters;
    }

    /**
     * Create a new {@link DocumentObject} for the given {@link NamedCsvRecord}.
     *
     * @param namedCsvRow the row to convert
     * @return a new {@link DocumentObject}
     */
    public DocumentObject create(final NamedCsvRecord namedCsvRow) {
        return new NamedCsvObjectNode(this.resourceName, this.converters, namedCsvRow);
    }

    /**
     * Create a new {@link DocumentObject} for the given {@link CsvRecord}.
     *
     * @param csvRow the row to convert
     * @return a new {@link DocumentObject}
     */
    public DocumentObject create(final CsvRecord csvRow) {
        return new CsvObjectNode(this.resourceName, this.converters, csvRow);
    }
}
