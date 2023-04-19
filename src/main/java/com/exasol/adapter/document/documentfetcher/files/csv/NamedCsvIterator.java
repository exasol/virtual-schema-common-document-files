package com.exasol.adapter.document.documentfetcher.files.csv;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.csv.NamedCsvObjectNode;

import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRow;

class NamedCsvIterator implements Iterator<DocumentNode> {

    private final Iterator<NamedCsvRow> delegate;

    NamedCsvIterator(final NamedCsvReader namedCsvReader) {
        this.delegate = namedCsvReader.iterator();
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
        final NamedCsvRow namedCsvRow = this.delegate.next();
        return new NamedCsvObjectNode(namedCsvRow);
    }
}
