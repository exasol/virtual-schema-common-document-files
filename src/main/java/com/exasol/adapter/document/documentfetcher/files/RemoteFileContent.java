package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.InMemoryRandomAccessStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.errorreporting.ExaError;

/**
 * Interface for classes that provide access to the content of {@link RemoteFile}s.
 */
public interface RemoteFileContent {
    /**
     * Get the {@link InputStream} with the contents for loaded file.
     *
     * @return {@link InputStream} with the contents for loaded file
     */
    public InputStream getInputStream();

    /**
     * Get a {@link RandomAccessInputStream} of the file.
     * <p>
     * If possible each datasource should override this method. Only use this default implementation if the data source
     * does not support random access.
     * </p>
     *
     * @return {@link RandomAccessInputStream} of the file.
     */
    public default RandomAccessInputStream getRandomAccessInputStream() {
        try (final InputStream inputStream = getInputStream()) {
            return InMemoryRandomAccessStream.getInMemoryCache(inputStream);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-11")
                    .message("Failed reading data source's input stream.").toString(), exception);
        }
    }

    /**
     * Load the file content asynchronously.
     * 
     * @return future with the content
     */
    public Future<byte[]> loadAsync();
}
