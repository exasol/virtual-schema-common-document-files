package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.*;

/**
 * This class implements a {@link RandomAccessInputStream} for local files.
 */
public class FileRandomAccessInputStream extends RandomAccessInputStream {
    private final RandomAccessFile fileReader;

    /**
     * Create a new instance of {@link FileRandomAccessInputStream}.
     * 
     * @param file file to open
     * @throws FileNotFoundException if open fails
     */
    public FileRandomAccessInputStream(final File file) throws FileNotFoundException {
        this.fileReader = new RandomAccessFile(file, "r");
    }

    @Override
    public void seek(final long position) throws IOException {
        this.fileReader.seek(position);
    }

    @Override
    public long getPos() throws IOException {
        return this.fileReader.getFilePointer();
    }

    @Override
    public long getLength() throws IOException {
        return this.fileReader.length();
    }

    @Override
    public int read() throws IOException {
        return this.fileReader.read();
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return this.fileReader.read(b);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.fileReader.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        this.fileReader.close();
        super.close();
    }
}
