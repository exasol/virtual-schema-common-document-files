package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import com.exasol.adapter.document.documentfetcher.files.InputDataException;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This class iterates the lines of a JSON-Lines file an creates for each line a JSON {@link DocumentNode}.
 */
class CsvIterator implements CloseableIterator<DocumentNode> {
    // efficient reading of characters, arrays and lines
    private final InputStreamReader inputStreamReader;
    private long lineCounter = 0;
    private final String resourceName;
    private boolean hasHeaders = false;
    private CsvReader csvReader;
    private NamedCsvReader namedCsvReader;

    /**
     * Create a new instance of {@link CsvIterator}.
     * 
     * @param csvFile file loader for the CSV file
     */
    CsvIterator(final RemoteFile csvFile, final CsvConfiguration csvConfiguration) {
        this.inputStreamReader = new InputStreamReader(csvFile.getContent().getInputStream());
        this.resourceName = csvFile.getResourceName();
        readOutConfiguration(csvConfiguration);
        // fetch the first line in the file to be read out
        if (hasHeaders) {
            namedCsvReader = NamedCsvReader.builder().build(inputStreamReader);
        } else {
            csvReader = CsvReader.builder().build(inputStreamReader);
        }
    }

    @Override
    public boolean hasNext() {
        if (hasHeaders) {
            return namedCsvReader.iterator().hasNext();
        } else {
            return csvReader.iterator().hasNext();
        }
    }

    @Override
    public DocumentNode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            if (hasHeaders) {
                final NamedCsvRow namedCsvRow = namedCsvReader.iterator().next();
                this.lineCounter++;
                return new CsvObjectNode(namedCsvRow);
            } else {
                final CsvRow csvRow = csvReader.iterator().next();
                this.lineCounter++;
                return new CsvObjectNode(csvRow);
            }
        } catch (Exception exception) {
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

    private void readOutConfiguration(CsvConfiguration csvConfiguration) {
        if (csvConfiguration != null) {
            hasHeaders = csvConfiguration.getHasHeaders();
        }
    }
}
