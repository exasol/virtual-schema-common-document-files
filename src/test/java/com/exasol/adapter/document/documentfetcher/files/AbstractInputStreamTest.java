package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public abstract class AbstractInputStreamTest {
    private final int testSize;
    private final byte[] testData;

    protected AbstractInputStreamTest(final byte[] testData) {
        this.testData = testData;
        this.testSize = testData.length;
    }

    protected abstract InputStream getInputStreamToTest();

    @Test
    void testRead() throws IOException {
        final InputStream stream = getInputStreamToTest();
        assertThat((byte) stream.read(), equalTo(this.testData[0]));
    }

    @Test
    void testReadAtEOF() throws IOException {
        final InputStream stream = getInputStreamToTest();
        stream.skip(this.testSize);
        assertThat(stream.read(), equalTo(-1));
    }

    @Test
    void testMark() throws IOException {
        final InputStream stream = getInputStreamToTest();
        stream.mark(10);
        stream.read();
        stream.reset();
        assertThat((byte) stream.read(), equalTo(this.testData[0]));
    }

    @Test
    void testMarkSupported() throws IOException {
        final InputStream stream = getInputStreamToTest();
        assertTrue(stream.markSupported());
    }

    @Test
    void testReadToArray() throws IOException {
        final InputStream stream = getInputStreamToTest();
        final byte[] result = new byte[this.testSize];
        stream.read(result);
        assertThat(result, equalTo(this.testData));
    }

    @Test
    void testReadToArrayEOF() throws IOException {
        final InputStream stream = getInputStreamToTest();
        final byte[] result = new byte[this.testSize + 1];
        assertThat(stream.read(result), equalTo(this.testSize));
    }
}