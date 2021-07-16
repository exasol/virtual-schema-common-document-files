package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.IOException;
import java.io.InputStream;

import com.exasol.errorreporting.ExaError;

/**
 * This abstract class extends the {@link InputStream} by an interface that allows you to access the data non linearly.
 */
public abstract class RandomAccessInputStream extends InputStream {
    private long markPosition;

    /**
     * Jump to a specific position.
     * 
     * @param position position to jump to
     * @throws IOException if something goes wrong
     */
    public abstract void seek(long position) throws IOException;

    /**
     * Get the current cursor position.
     * 
     * @return current position
     * @throws IOException if something goes wrong
     */
    public abstract long getPos() throws IOException;

    /**
     * Get the length of the file.
     * 
     * @return length of the file
     * @throws IOException if something goes wrong
     */
    public abstract long getLength() throws IOException;

    @Override
    public synchronized void mark(final int readlimit) {
        try {
            this.markPosition = getPos();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-VSDF-10").message("Failed to mark position.").toString(), exception);
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        this.seek(this.markPosition);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public long skip(final long bytesToSkip) throws IOException {
        seek(getPos() + bytesToSkip);
        return bytesToSkip;
    }
}
