package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;

import com.exasol.adapter.document.documentfetcher.files.InputDataException;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.mapping.ColumnMapping;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.*;

/**
 * This class iterates the lines of a CSV file and creates a {@link CsvObjectNode} for each line.
 */
class CsvIterator implements CloseableIterator<DocumentNode> {
    // efficient reading of characters, arrays and lines
    private final InputStreamReader inputStreamReader;
    private final List<ColumnMapping> csvColumns;
    private final String resourceName;
    private long lineCounter = 0;
    private boolean hasHeaders = false;
    private CsvReader csvReader;
    private NamedCsvReader namedCsvReader;

    /**
     * Create a new instance of {@link CsvIterator}.
     *
     * @param csvFile    file loader for the CSV file
     * @param csvColumns
     */
    CsvIterator(final RemoteFile csvFile, final List<ColumnMapping> csvColumns,
            final CsvConfiguration csvConfiguration) {
        this.csvColumns = csvColumns;
        this.inputStreamReader = new InputStreamReader(csvFile.getContent().getInputStream());
        this.resourceName = csvFile.getResourceName();
        readOutConfiguration(csvConfiguration);
        // fetch the first line in the file to be read out
        if (this.hasHeaders) {
            this.namedCsvReader = NamedCsvReader.builder().build(this.inputStreamReader);
        } else {
            this.csvReader = CsvReader.builder().build(this.inputStreamReader);
        }
    }

    @Override
    public boolean hasNext() {
        if (this.hasHeaders) {
            return this.namedCsvReader.iterator().hasNext();
        } else {
            return this.csvReader.iterator().hasNext();
        }
    }

    @Override
    public DocumentNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            if (this.hasHeaders) {
                final NamedCsvRow namedCsvRow = this.namedCsvReader.iterator().next();
                this.lineCounter++;
                return new CsvObjectNode(namedCsvRow);
            } else {
                final CsvRow csvRow = this.csvReader.iterator().next();
                this.lineCounter++;
                return new CsvObjectNode(csvRow);
            }
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

    private void readOutConfiguration(final CsvConfiguration csvConfiguration) {
        if (csvConfiguration != null) {
            this.hasHeaders = csvConfiguration.getHasHeaders();
        }
    }
}
