package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.InputDataException;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

import java.io.*;
import java.util.NoSuchElementException;

/**
 * This class iterates the lines of a JSON-Lines file an creates for each line a JSON {@link DocumentNode}.
 */
class CsvIterator implements CloseableIterator<DocumentNode> {
    //private static final JsonProvider JSON = JsonProvider.provider();
    //efficient reading of characters, arrays and lines
    private final BufferedReader csvBufferedReader;
    private final InputStreamReader inputStreamReader;
    private String nextLine = null;
    private long lineCounter = 0;
    private final String resourceName;

    /**
     * Create a new instance of {@link CsvIterator}.
     * 
     * @param csvFile file loader for the JSON-Lines file
     */
    CsvIterator(final RemoteFile csvFile) {
        this.inputStreamReader = new InputStreamReader(csvFile.getContent().getInputStream());
        this.csvBufferedReader = new BufferedReader(this.inputStreamReader);
        this.resourceName = csvFile.getResourceName();
        //fetch the first line in the file to be read out
        readNextLine();
    }

    //add support for custom settings like headers and custom delimiters
    private CsvReader buildCsvReader(final String csvRowString) {
        return CsvReader.builder().build(csvRowString);
    }
    private void readNextLine() {
        try {
            do {
                //put the next line into the nextline string
                this.nextLine = this.csvBufferedReader.readLine();
                //add to the linecounter
                this.lineCounter++;
            } while (this.nextLine != null && this.nextLine.isBlank());
        } catch (final IOException exception) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-24").message("Failed to read from data file {{CSV_FILE}}.")
                            .parameter("CSV_FILE", this.resourceName).toString(),
                    exception);
        }
    }

    @Override
    public boolean hasNext() {
        return this.nextLine != null;
    }

    @Override
    public DocumentNode next() {
        if (this.nextLine == null) {
            throw new NoSuchElementException();
        }
        try (final CsvReader csvReader = buildCsvReader(nextLine)) {
            //read out the value
            //you could probably rewrite this in a smarter way with 1 builder for the whole csv File or csv segment but as long as it works + this is probably just as memory efficient.
            final CsvRow csvRow = csvReader.iterator().next();
            //fetch the next line in the file to be read out
            readNextLine();
            //create the node
            return new CsvObjectNode(csvRow);
        } catch (IOException exception) {
            throw new InputDataException(ExaError.messageBuilder("E-VSDF-25").message(
                            "Failed to parse CSV-Lines from {{CSV_FILE}}. Invalid CSV row in line {{LINE}}.")
                    .parameter("CSV_FILE", this.resourceName).parameter("LINE", this.lineCounter).toString(),
                    exception);
        }
    }

    @Override
    public void close() {
        try {
            this.inputStreamReader.close();
            this.csvBufferedReader.close();
        } catch (final IOException exception) {
            // at least we tried...
        }
    }
}
