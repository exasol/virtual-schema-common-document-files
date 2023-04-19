package com.exasol.adapter.document.documentfetcher.files.csv;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.exasol.adapter.document.documentnode.DocumentNode;

class ConvertingCsvIterator<T> implements Iterator<DocumentNode> {

    private final Iterator<T> delegate;
    private final Function<T, DocumentNode> converter;

    ConvertingCsvIterator(final Iterable<T> delegate, final Function<T, DocumentNode> converter) {
        this.delegate = delegate.iterator();
        this.converter = converter;
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
        final T csvRow = this.delegate.next();
        return converter.apply(csvRow);
    }
}
