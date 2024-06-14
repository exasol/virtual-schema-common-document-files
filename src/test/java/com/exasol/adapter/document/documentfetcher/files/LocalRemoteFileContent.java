package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
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
        try {
            return CompletableFuture.completedFuture(Files.readAllBytes(localFile));
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    "Error loading file content of '" + this.localFile + "': " + exception.getMessage(), exception);
        }
    }
}
