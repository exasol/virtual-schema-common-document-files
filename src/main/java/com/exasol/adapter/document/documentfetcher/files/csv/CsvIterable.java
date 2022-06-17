package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;

import java.util.Iterator;

/**
 * This class provides a {@link CsvIterator} for a given CSV file.
 */
class CsvIterable implements Iterable<DocumentNode> {
    private final RemoteFile csvFile;
    private final CsvConfiguration csvConfiguration;

    /**
     * Create a new instance of {@link CsvIterable}.
     * 
     * @param csvFile CSV file
     */
    CsvIterable(final RemoteFile csvFile,final CsvConfiguration csvConfiguration) {

        this.csvFile = csvFile;
        this.csvConfiguration = csvConfiguration;
    }

    @Override
    public Iterator<DocumentNode> iterator() {
        return new CsvIterator(this.csvFile,this.csvConfiguration);
    }
}
