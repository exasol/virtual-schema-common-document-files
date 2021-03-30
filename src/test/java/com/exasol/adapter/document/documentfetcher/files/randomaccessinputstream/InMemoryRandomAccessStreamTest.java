package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

public class InMemoryRandomAccessStreamTest extends RandomAccessInputStreamTestBase {
    byte[] testData;

    @Override
    protected void prepareTestSetup(final byte[] testData) {
        this.testData = testData;
    }

    @Override
    protected RandomAccessInputStream getSeekableInputStream() {
        return new InMemoryRandomAccessStream(this.testData);
    }
}
