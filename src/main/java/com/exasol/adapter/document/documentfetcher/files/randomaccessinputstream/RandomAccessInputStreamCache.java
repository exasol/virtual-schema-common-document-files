package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.IOException;

/**
 * This class is a wrapper for {@link RandomAccessInputStream}s that caches some data to avoid many small requests.
 */
public class RandomAccessInputStreamCache extends RandomAccessInputStream {
    private final RandomAccessInputStream source;
    private final byte[] cache;
    private long cacheStartPos = 0;
    private long usedCacheSize = 0;
    private long position = 0;

    /**
     * Create a new instance of {@link RandomAccessInputStream}.
     * 
     * @param source    source to wrap
     * @param cacheSize size of the cache
     */
    public RandomAccessInputStreamCache(final RandomAccessInputStream source, final int cacheSize) {
        this.source = source;
        this.cache = new byte[cacheSize];
    }

    @Override
    public void seek(final long position) {
        this.position = position;
    }

    @Override
    public long getPos() {
        return this.position;
    }

    @Override
    public long getLength() throws IOException {
        return this.source.getLength();
    }

    @Override
    public int read() throws IOException {
        if (this.position < this.getLength()) {
            if (!isCurrentPositionInCache()) {
                fillCache();
            }
            final long positionInCache = this.position - this.cacheStartPos;
            this.position++;
            return this.cache[(int) positionInCache] & 0xFF;
        } else {
            return -1;
        }
    }

    private void fillCache() throws IOException {
        this.source.seek(this.position);
        this.usedCacheSize = this.source.read(this.cache, 0, this.cache.length);
        this.cacheStartPos = this.position;
    }

    @Override
    public int read(final byte[] destination, final int offset, final int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        final long remainingBytesInStream = getLength() - this.position;
        final int actualReadLength = (int) Math.min(length, remainingBytesInStream);
        if (actualReadLength > 0) {
            final long positionInCache = this.position - this.cacheStartPos;
            final long cachedSize = getCachedSize(actualReadLength, positionInCache);
            if (positionInCache >= 0 && cachedSize > 0) {
                System.arraycopy(this.cache, (int) positionInCache, destination, offset, (int) cachedSize);
                this.position += cachedSize;
            }
            final long remainingSize = actualReadLength - cachedSize;
            if (remainingSize > 0) {// not everything requested was in the cache so we have to fetch it
                return (int) cachedSize + readRemaining(destination, offset, cachedSize, remainingSize);
            }
        }
        return actualReadLength == 0 ? -1 : actualReadLength;
    }

    private long getCachedSize(final int actualReadLength, final long positionInCache) {
        if (positionInCache < 0) {
            return 0;
        } else {
            return Math.max(Math.min(actualReadLength, this.usedCacheSize - positionInCache), 0);
        }
    }

    private int readRemaining(final byte[] destination, final int offset, final long cachedSize,
            final long remainingSize) throws IOException {
        this.source.seek(this.position);
        if (remainingSize < this.cache.length) {
            // request is smaller than cache so we will the cache and answer the request from it
            final int bytesRead = this.source.read(this.cache, 0, this.cache.length);
            final long requestedReadBytes = Math.min(bytesRead, remainingSize);
            // copy from cache to destination array
            System.arraycopy(this.cache, 0, destination, (int) (offset + cachedSize), (int) requestedReadBytes);
            this.cacheStartPos = this.position;
            this.position += requestedReadBytes;
            this.usedCacheSize = bytesRead;
            return (int) requestedReadBytes;
        } else {
            // request is larger than cache so we simply answer it by requesting the data from the source
            final int bytesRead = this.source.read(destination, (int) (offset + cachedSize), (int) remainingSize);
            this.position += bytesRead;
            return bytesRead;
        }
    }

    private boolean isCurrentPositionInCache() {
        return this.cacheStartPos <= this.position && this.usedCacheSize > 0;
    }
}
