package com.exasol.adapter.document.documentfetcher.files;

import java.io.InputStream;
import java.util.concurrent.Future;

class AssertStreamIsClosedRemoteFileContent implements RemoteFileContent {
    private final CloseCheckStream closeCheckStream;

    public AssertStreamIsClosedRemoteFileContent(final String content) {
        this.closeCheckStream = new CloseCheckStream(content);
    }

    @Override
    public InputStream getInputStream() {
        return this.closeCheckStream;
    }

    @Override
    public Future<byte[]> loadAsync() {
        throw new UnsupportedOperationException();
    }

    public boolean isStreamClosed() {
        return this.closeCheckStream.wasClosed();
    }

}
