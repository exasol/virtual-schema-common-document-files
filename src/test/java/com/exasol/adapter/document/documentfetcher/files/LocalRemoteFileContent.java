package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Future;

public class LocalRemoteFileContent implements RemoteFileContent {
    private final Path localFile;

    public LocalRemoteFileContent(final Path localFile) {
        this.localFile = localFile;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(this.localFile.toFile());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    public Future<byte[]> loadAsync() {
        throw new UnsupportedOperationException();
    }
}
