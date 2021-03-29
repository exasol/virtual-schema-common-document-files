package com.exasol.adapter.document.documentfetcher.files;

import java.io.InputStream;

class AssertStreamIsClosedLoadedFile extends LoadedFile {
    private final CloseCheckStream closeCheckStream;

    public AssertStreamIsClosedLoadedFile(final String content) {
        super("");
        this.closeCheckStream = new CloseCheckStream(content);
    }

    @Override
    public InputStream getInputStream() {
        return this.closeCheckStream;
    }

    public boolean isStreamClosed() {
        return this.closeCheckStream.wasClosed();
    }
}
