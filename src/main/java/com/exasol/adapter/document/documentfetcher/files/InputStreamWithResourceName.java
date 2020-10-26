package com.exasol.adapter.document.documentfetcher.files;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class describes a file loaded by the {@link FileLoader}.
 */
public class InputStreamWithResourceName implements Closeable {
    private final InputStream inputStream;
    private final String resourceName;

    /**
     * Create a new instance of {@link InputStreamWithResourceName}.
     * 
     * @param inputStream  stream of file contents
     * @param resourceName description of the file e.g. file name; use for error messages
     */
    public InputStreamWithResourceName(final InputStream inputStream, final String resourceName) {
        this.inputStream = inputStream;
        this.resourceName = resourceName;
    }

    /**
     * Get the {@link InputStream} with the contents for loaded file.
     * 
     * @return {@link InputStream} with the contents for loaded file
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * Get the description of the file e.g. file name; use for error messages
     * 
     * @return description of the file
     */
    public String getResourceName() {
        return this.resourceName;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
