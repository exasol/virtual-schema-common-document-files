package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.IOException;
import java.io.InputStream;

import com.exasol.errorreporting.ExaError;

/**
 * {@link RandomAccessInputStream} that simply loads all contents of an input stream to an byte array and by that offers
 * random access.
 */
public class InMemoryRandomAccessStream extends RandomAccessInputStream {
    private final byte[] data;
    long position = 0;

    public InMemoryRandomAccessStream(final byte[] data) {
        this.data = data;
    }

    /**
     * Create an {@link InMemoryRandomAccessStream} with the content from a regular {@link InputStream}.
     *
     * @param normalInputStream input stream to read from
     * @return built {@link InMemoryRandomAccessStream}
     */
    public static InMemoryRandomAccessStream getInMemoryCache(final InputStream normalInputStream) {
        try {
            return new InMemoryRandomAccessStream(normalInputStream.readAllBytes());
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-12").message(
                    "This data source does not support random access for files. So we tried to keep everything in memory, but it failed (probably due to too few memory).")
                    .mitigation("Reduce your file size.")
                    .mitigation("Use a different data source that supports random access.").toString());
        }
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
    public long getLength() {
        return this.data.length;
    }

    @Override
    public int read() throws IOException {
        if (this.position < this.data.length) {
            final byte readByte = this.data[(int) this.position];
            this.position++;
            return readByte & 0xFF;
        } else {
            return -1;
        }
    }
}
