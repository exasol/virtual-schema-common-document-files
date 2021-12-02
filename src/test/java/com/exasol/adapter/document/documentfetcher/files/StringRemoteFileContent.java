package com.exasol.adapter.document.documentfetcher.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.concurrent.CompletedFuture;

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
    public Future<byte[]> loadAssync() {
        return new CompletedFuture<>(this.value.getBytes(StandardCharsets.UTF_8));
    }
}
