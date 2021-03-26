package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.*;

class RandomAccessInputStreamReferenceImplementation extends RandomAccessInputStream {
    private final RandomAccessFile fileReader;

    RandomAccessInputStreamReferenceImplementation(final File file) throws FileNotFoundException {
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
    public void close() throws IOException {
        super.close();
        this.fileReader.close();
    }
}
