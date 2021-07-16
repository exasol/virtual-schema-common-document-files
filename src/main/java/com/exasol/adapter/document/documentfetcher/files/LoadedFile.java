package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.InputStream;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.InMemoryRandomAccessStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.errorreporting.ExaError;

/**
 * This class describes a file loaded by the {@link FileLoader}.
 */
public abstract class LoadedFile {
    private final String resourceName;

    /**
     * Create a new instance of {@link LoadedFile}.
     *
     * @param resourceName description of the file e.g. file name; use for error messages
     */
    @SuppressWarnings("java:S5993") // public so that sub classes from other packages can call it
    public LoadedFile(final String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Get the {@link InputStream} with the contents for loaded file.
     * 
     * @return {@link InputStream} with the contents for loaded file
     */
    public abstract InputStream getInputStream();

    /**
     * Get a {@link RandomAccessInputStream} of the file.
     * <p>
     * If possible each datasource should override this method. Only use this default implementation if the data source
     * does not support random access.
     * </p>
     * 
     * @return {@link RandomAccessInputStream} of the file.
     */
    public RandomAccessInputStream getRandomAccessInputStream() {
        try (final InputStream inputStream = getInputStream()) {
            return InMemoryRandomAccessStream.getInMemoryCache(inputStream);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-11")
                    .message("Failed reading data source's input stream.").toString(), exception);
        }
    }

    /**
     * Get the description of the file e.g. file name; use for error messages
     * 
     * @return description of the file
     */
    public String getResourceName() {
        return this.resourceName;
    }
}
