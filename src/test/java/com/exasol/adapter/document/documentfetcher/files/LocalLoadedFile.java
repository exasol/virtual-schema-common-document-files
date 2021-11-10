package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Path;

public class LocalLoadedFile extends RemoteFile {
    private final Path localFile;

    public LocalLoadedFile(final Path localFile) {
        super(localFile.toFile().getName());
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
}
