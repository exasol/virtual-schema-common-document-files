package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.parquet.io.SeekableInputStream;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;

/**
 * Adapted from https://github.com/haifengl/smile/blob/master/io/src/main/java/smile/io/LocalInputFile.java
 */
public final class SeekableInputStreamAdapter {
    private static final int COPY_BUFFER_SIZE = 8192;

    public static org.apache.parquet.io.InputFile convert(final RandomAccessInputStream source) {

        return new org.apache.parquet.io.InputFile() {
            @Override
            public long getLength() throws IOException {
                return source.getLength();
            }

            @Override
            public SeekableInputStream newStream() throws IOException {
                return new SeekableInputStream() {
                    private final byte[] tmpBuf = new byte[COPY_BUFFER_SIZE];

                    @Override
                    public int read() throws IOException {
                        return source.read();
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public int read(final byte[] b) throws IOException {
                        return source.read(b);
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public int read(final byte[] b, final int off, final int len) throws IOException {
                        return source.read(b, off, len);
                    }

                    @Override
                    public long skip(final long n) throws IOException {
                        return source.skip(n);
                    }

                    @Override
                    public int available() throws IOException {
                        return source.available();
                    }

                    @Override
                    public void close() throws IOException {
                        source.close();
                    }

                    @Override
                    public long getPos() throws IOException {
                        return source.getPos();
                    }

                    @Override
                    public void seek(final long l) throws IOException {
                        source.seek(l);
                    }

                    @Override
                    public boolean markSupported() {
                        return source.markSupported();
                    }

                    @Override
                    public synchronized void mark(final int readLimit) {
                        source.mark(readLimit);
                    }

                    @Override
                    public synchronized void reset() throws IOException {
                        source.reset();
                    }

                    @Override
                    public void readFully(final byte[] bytes) throws IOException {
                        readFully(bytes, 0, bytes.length);
                    }

                    @Override
                    public void readFully(final byte[] bytes, final int offset, final int length) throws IOException {
                        int n = 0;
                        do {
                            final int count = this.read(bytes, offset + n, length - n);
                            if (count < 0)
                                throw new EOFException();
                            n += count;
                        } while (n < length);
                    }

                    @Override
                    public int read(final ByteBuffer byteBuffer) throws IOException {
                        return readDirectBuffer(byteBuffer, this.tmpBuf, source::read);
                    }

                    @Override
                    public void readFully(final ByteBuffer byteBuffer) throws IOException {
                        readFullyDirectBuffer(byteBuffer, this.tmpBuf, source::read);
                    }
                };
            }
        };
    }

    private static int readDirectBuffer(final ByteBuffer byteBufr, final byte[] tmpBuf, final ByteBufReader rdr)
            throws IOException {
        // copy all the bytes that return immediately, stopping at the first
        // read that doesn't return a full buffer.
        int nextReadLength = Math.min(byteBufr.remaining(), tmpBuf.length);
        int totalBytesRead = 0;
        int bytesRead;

        while ((bytesRead = rdr.read(tmpBuf, 0, nextReadLength)) == tmpBuf.length) {
            byteBufr.put(tmpBuf);
            totalBytesRead += bytesRead;
            nextReadLength = Math.min(byteBufr.remaining(), tmpBuf.length);
        }

        if (bytesRead < 0) {
            // return -1 if nothing was read
            return totalBytesRead == 0 ? -1 : totalBytesRead;
        } else {
            // copy the last partial buffer
            byteBufr.put(tmpBuf, 0, bytesRead);
            totalBytesRead += bytesRead;
            return totalBytesRead;
        }
    }

    private static void readFullyDirectBuffer(final ByteBuffer byteBufr, final byte[] tmpBuf, final ByteBufReader rdr)
            throws IOException {
        int nextReadLength = Math.min(byteBufr.remaining(), tmpBuf.length);
        int bytesRead = 0;

        while (nextReadLength > 0 && (bytesRead = rdr.read(tmpBuf, 0, nextReadLength)) >= 0) {
            byteBufr.put(tmpBuf, 0, bytesRead);
            nextReadLength = Math.min(byteBufr.remaining(), tmpBuf.length);
        }

        if (bytesRead < 0 && byteBufr.remaining() > 0) {
            throw new EOFException("Reached the end of stream with " + byteBufr.remaining() + " bytes left to read");
        }
    }

    @FunctionalInterface
    private interface ByteBufReader {
        int read(byte[] b, int off, int len) throws IOException;
    }
}