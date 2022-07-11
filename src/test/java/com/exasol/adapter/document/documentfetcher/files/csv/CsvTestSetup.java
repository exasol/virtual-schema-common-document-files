package com.exasol.adapter.document.documentfetcher.files.csv;

import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvTestSetup {
    private final Path csvFile;
    private final CsvWriter csvWriter;

    public CsvTestSetup(final Path tempDir, final List<String> columns) throws IOException {
        this.csvFile = Files.createTempFile(tempDir, "testData", ".csv");
        //Files.delete(this.csvFile); //TODO: this doesn't seem to make much sense, check in parquet test setup as well???
        this.csvWriter = CsvWriter.builder().build(new FileWriter(csvFile.toFile()));
        //todo: maybe write headers with writeRow(columns)
        this.csvWriter.writeRow(columns);
    }

    public Path getCsvFile() {
        return this.csvFile;
    }

    public CsvTestSetup writeRow(final List<String > values) throws IOException {

        this.csvWriter.writeRow(values);
        return this;
    }

    public void closeWriter() throws IOException {
        this.csvWriter.close();
    }
}
