package com.exasol.adapter.document.documentfetcher.files.csv;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

class SimpleCsvIterator implements Iterator<DocumentNode> {
    private final Iterator<CsvRow> delegate;

    SimpleCsvIterator(final CsvReader csvReader) {
        this.delegate = csvReader.iterator();
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
        final CsvRow csvRow = this.delegate.next();
        return new CsvObjectNode(csvRow);
    }
}
