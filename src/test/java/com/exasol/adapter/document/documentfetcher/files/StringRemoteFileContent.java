package com.exasol.adapter.document.documentfetcher.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


public class StringRemoteFileContent implements RemoteFileContent {
    private final String value;

    public StringRemoteFileContent(final String value) {
        this.value = value;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.value.getBytes());
    }

    @Override
    public Future<byte[]> loadAsync() {
        return CompletableFuture.completedFuture(this.value.getBytes(StandardCharsets.UTF_8));
    }
}
