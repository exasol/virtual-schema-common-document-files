package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.IOException;

class RandomAccessInputStreamCacheTest extends RandomAccessInputStreamTestBase {
    private byte[] testData;

    @Override
    protected void prepareTestSetup(final byte[] testData) throws IOException {
        this.testData = testData;
    }

    @Override
    protected RandomAccessInputStream getSeekableInputStream() {
        return new RandomAccessInputStreamCache(new InMemoryRandomAccessStream(this.testData), 5000);
    }
}