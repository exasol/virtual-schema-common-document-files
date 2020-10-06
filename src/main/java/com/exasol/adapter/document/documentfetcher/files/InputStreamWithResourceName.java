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
    private final String relativeResourceName;

    /**
     * Create a new instance of {@link InputStreamWithResourceName}.
     * 
     * @param inputStream          stream of file contents
     * @param resourceName         description of the file e.g. file name; use for error messages
     * @param relativeResourceName resource name relative to the path configured in the CONNECTION
     */
    public InputStreamWithResourceName(final InputStream inputStream, final String resourceName,
            final String relativeResourceName) {
        this.inputStream = inputStream;
        this.resourceName = resourceName;
        this.relativeResourceName = relativeResourceName;
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
    public String getFullResourceName() {
        return this.resourceName;
    }

    /**
     * Get the resource name relative to the path configured in the CONNECTION.
     * <p>
     * Used as value of the RESOURCE_REFERENCE column.
     * </p>
     *
     * @return resource name
     */
    public String getRelativeResourceName() {
        return this.relativeResourceName;
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
