package com.exasol.adapter.document.documentfetcher.files;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class describes a file loaded by the {@link FileLoader}.
 */
public class LoadedFile implements Closeable {
    private final InputStream inputStream;
    private final String fileDescription;

    /**
     * Create a new instance of {@link LoadedFile}.
     * 
     * @param inputStream     stream of file contents
     * @param fileDescription description of the file e.g. file name; use for error messages
     */
    public LoadedFile(final InputStream inputStream, final String fileDescription) {
        this.inputStream = inputStream;
        this.fileDescription = fileDescription;
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
    public String getFileDescription() {
        return this.fileDescription;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
