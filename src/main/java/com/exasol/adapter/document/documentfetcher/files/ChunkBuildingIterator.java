package com.exasol.adapter.document.documentfetcher.files;

import java.util.*;

/**
 * This class is a wrapper for {@link Iterator} that groups the data in chunks.
 * 
 * @param <T> type of the wrapped iterator
 */
public class ChunkBuildingIterator<T> implements Iterator<List<T>> {
    private static final int CHUNK_SIZE = 10000;
    private final Iterator<T> source;
    private boolean hasNext = false;
    private List<T> nextChunk;

    /**
     * Create a new instance of {@link ChunkBuildingIterator}.
     * 
     * @param source iterator to wrap
     */
    public ChunkBuildingIterator(final Iterator<T> source) {
        this.source = source;
        buildChunk();
    }

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public List<T> next() {
        if (!this.hasNext) {
            throw new NoSuchElementException();// TODO add message
        }
        final List<T> thisChunk = this.nextChunk;
        buildChunk();
        return thisChunk;
    }

    private void buildChunk() {
        this.nextChunk = new ArrayList<>(CHUNK_SIZE);
        for (int counter = 0; counter < CHUNK_SIZE && this.source.hasNext(); counter++) {
            this.nextChunk.add(this.source.next());
        }
        this.hasNext = !this.nextChunk.isEmpty();
    }
}
