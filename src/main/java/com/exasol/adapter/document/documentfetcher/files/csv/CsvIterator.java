package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.exasol.adapter.document.documentfetcher.files.InputDataException;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.mapping.ColumnMapping;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvReader;

/**
 * This class iterates the lines of a CSV file and creates a {@link CsvObjectNode} for each line.
 */
class CsvIterator implements CloseableIterator<DocumentNode> {
    // efficient reading of characters, arrays and lines
    private final InputStreamReader inputStreamReader;
    private final List<ColumnMapping> csvColumns;
    private final String resourceName;
    private long lineCounter = 0;
    private final Iterator<DocumentNode> delegate;

    private CsvIterator(final InputStreamReader inputStreamReader, final String resourceName,
            final Iterator<DocumentNode> delegate, final List<ColumnMapping> csvColumns) {
        this.inputStreamReader = inputStreamReader;
        this.resourceName = resourceName;
        this.delegate = delegate;
        this.csvColumns = csvColumns;
    }

    /**
     * Create a new instance of {@link CsvIterator}.
     *
     * @param csvFile          file loader for the CSV file
     * @param csvColumns       columns of the CSV file
     * @param csvConfiguration CSV file configuration
     */
    static CsvIterator create(final RemoteFile csvFile, final List<ColumnMapping> csvColumns,
            final CsvConfiguration csvConfiguration) {
        final InputStreamReader inputStreamReader = new InputStreamReader(csvFile.getContent().getInputStream());
        final String resourceName = csvFile.getResourceName();
        final Iterator<DocumentNode> delegate = createDelegate(csvConfiguration, inputStreamReader);
        return new CsvIterator(inputStreamReader, resourceName, delegate, csvColumns);
    }

    private static Iterator<DocumentNode> createDelegate(final CsvConfiguration csvConfiguration,
            final InputStreamReader inputStreamReader) {
        if (hasHeaders(csvConfiguration)) {
            return new NamedCsvIterator(NamedCsvReader.builder().build(inputStreamReader));
        } else {
            return new SimpleCsvIterator(CsvReader.builder().build(inputStreamReader));
        }
    }

    private static boolean hasHeaders(final CsvConfiguration csvConfiguration) {
        if (csvConfiguration != null) {
            return csvConfiguration.getHasHeaders();
        }
        return false;
    }

    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override
    public DocumentNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            this.lineCounter++;
            return this.delegate.next();
        } catch (final Exception exception) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-25")
                            .message("Failed to parse CSV-Lines from {{CSV_FILE}}. Invalid CSV row in line {{LINE}}.")
                            .parameter("CSV_FILE", this.resourceName).parameter("LINE", this.lineCounter).toString(),
                    exception);
        }
    }

    @Override
    public void close() {
        try {
            this.inputStreamReader.close();
        } catch (final IOException exception) {
            // at least we tried...
        }
    }
}
