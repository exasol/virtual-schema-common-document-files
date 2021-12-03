package com.exasol.adapter.document.documentfetcher.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.InMemoryRandomAccessStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.errorreporting.ExaError;

/**
 * {@link RemoteFileContent} wrapper for byte array.
 */
public class InMemoryRemoteFileContent implements RemoteFileContent {
    private final byte[] content;

    /**
     * Create a new instance of {@link InMemoryRemoteFileContent}.
     *
     * @param content file content
     */
    public InMemoryRemoteFileContent(final byte[] content) {
        this.content = content;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public RandomAccessInputStream getRandomAccessInputStream() {
        return new InMemoryRandomAccessStream(this.content);
    }

    @Override
    public Future<byte[]> loadAsync() {
        throw new UnsupportedOperationException(ExaError.messageBuilder("F-VSDF-18")
                .message("InMemoryRemoteFile does not support asynchronous access.").ticketMitigation().toString());
    }
}
