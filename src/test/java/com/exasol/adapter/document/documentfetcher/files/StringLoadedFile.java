package com.exasol.adapter.document.documentfetcher.files;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringLoadedFile extends RemoteFile {
    private final String value;

    public StringLoadedFile(final String value, final String resourceName) {
        super(resourceName);
        this.value = value;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.value.getBytes());
    }
}
