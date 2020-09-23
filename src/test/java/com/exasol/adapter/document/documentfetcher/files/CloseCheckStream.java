package com.exasol.adapter.document.documentfetcher.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class CloseCheckStream extends ByteArrayInputStream {
    private boolean wasClosed = false;

    public CloseCheckStream(final String content) {
        super(content.getBytes());
    }

    @Override
    public void close() throws IOException {
        this.wasClosed = true;
        super.close();
    }

    public boolean wasClosed() {
        return this.wasClosed;
    }
}
