package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.*;
import java.util.Arrays;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.documentfetcher.files.AbstractInputStreamTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class RandomAccessInputStreamTestBase extends AbstractInputStreamTest {
    private static final byte[] testData = generateTestData(50000);
    private RandomAccessInputStream seekableStream;

    protected RandomAccessInputStreamTestBase() {
        super(testData);
    }

    private static byte[] generateTestData(final int testSize) {
        final byte[] bytes = new byte[testSize];
        for (int count = 0; count < testSize; count++) {
            bytes[count] = (byte) ((count * 23) % 200);// simple pseudo random
        }
        return bytes;
    }

    @Override
    protected InputStream getInputStreamToTest() {
        return this.seekableStream;
    }

    protected abstract void prepareTestSetup(byte[] testData) throws IOException;

    /**
     * This method is a hook for deleting the test setup created in {@link #prepareTestSetup(byte[])}.
     * 
     * @throws IOException if cleaning fails
     */
    protected void cleanupTestSetup() throws IOException {
        // nothing to do.
    }

    protected abstract RandomAccessInputStream getSeekableInputStream();

    @BeforeAll
    void beforeAll() throws IOException {
        this.prepareTestSetup(testData);
    }

    @AfterAll
    void afterAll() throws IOException {
        this.cleanupTestSetup();
    }

    @BeforeEach
    void beforeEach() throws FileNotFoundException {
        this.seekableStream = getSeekableInputStream();
    }

    @Test
    void testNormalRead() throws IOException {
        assertThat(this.seekableStream.readAllBytes(), equalTo(testData));
    }

    @Test
    void testSeekAndRead() throws IOException {
        this.seekableStream.seek(2);
        assertThat((byte) this.seekableStream.read(), equalTo(testData[2]));
    }

    @Test
    void testSkipAndRead() throws IOException {
        int bytesToSkip = 2;
        while (bytesToSkip > 0) {
            bytesToSkip -= this.seekableStream.skip(bytesToSkip);
        }
        assertThat((byte) this.seekableStream.read(), equalTo(testData[2]));
    }

    @Test
    void testBackwardSeek() throws IOException {
        this.seekableStream.readAllBytes();
        this.seekableStream.seek(0);
        final byte[] result = this.seekableStream.readAllBytes();
        assertThat("data was different", Arrays.equals(result, testData));
    }

    @Test
    void testSeekOutOfRange() throws IOException {
        assertDoesNotThrow(() -> this.seekableStream.seek(testData.length + 1));
    }

    @Test
    void testArrayReadAfterSeek() throws IOException {
        final byte[] buffer = new byte[3];
        this.seekableStream.seek(2);
        this.seekableStream.read(buffer, 0, 3);
        assertThat(buffer, equalTo(Arrays.copyOfRange(testData, 2, 2 + 3)));
    }

    @Test
    void testGetPosAfterInit() throws IOException {
        assertThat(this.seekableStream.getPos(), equalTo(0L));
    }

    @Test
    void testGetPosAfterRead() throws IOException {
        this.seekableStream.read();
        assertThat(this.seekableStream.getPos(), equalTo(1L));
    }

    @Test
    void testGetPosAfterSeek() throws IOException {
        this.seekableStream.seek(2);
        assertThat(this.seekableStream.getPos(), equalTo(2L));
    }

    @Test
    void testGetLength() throws IOException {
        assertThat(this.seekableStream.getLength(), equalTo((long) testData.length));
    }
}