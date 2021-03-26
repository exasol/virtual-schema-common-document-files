package com.exasol.adapter.document.documentfetcher.files;

import java.io.InputStream;

class CloseCheckLoadedFile extends LoadedFile {
    private final CloseCheckStream closeCheckStream;

    public CloseCheckLoadedFile(final String content) {
        super("");
        this.closeCheckStream = new CloseCheckStream(content);
    }

    @Override
    public InputStream getInputStream() {
        return this.closeCheckStream;
    }

    public boolean wasStreamClosed() {
        return this.closeCheckStream.wasClosed();
    }
}
