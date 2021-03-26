package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.SeekableInputStream;
import org.junit.jupiter.api.*;

import com.exasol.adapter.document.documentfetcher.files.AbstractInputStreamTest;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.InMemoryRandomAccessStream;

class SeekableInputStreamAdapterTest extends AbstractInputStreamTest {
    private static final int TEST_SIZE = 10000;
    private static byte[] someBytes;
    private InputFile inputFile;
    private SeekableInputStream stream;

    protected SeekableInputStreamAdapterTest() {
        super(someBytes);
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        someBytes = new byte[TEST_SIZE];
        for (int count = 0; count < TEST_SIZE; count++) {
            someBytes[count] = (byte) ((count * 23) % 200);// simple pseudo random
        }
    }

    @Override
    protected InputStream getInputStreamToTest() {
        return this.stream;
    }

    @BeforeEach
    void beforeEach() throws IOException {
        final InMemoryRandomAccessStream randomAccessInputStream = new InMemoryRandomAccessStream(someBytes);
        this.inputFile = SeekableInputStreamAdapter.convert(randomAccessInputStream);
        this.stream = this.inputFile.newStream();
    }

    @Test
    void testLength() throws IOException {
        assertThat(this.inputFile.getLength(), equalTo((long) someBytes.length));
    }

    @Test
    void testReadFullyToByteBuffer() throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        this.stream.readFully(byteBuffer);
        assertThat(byteBuffer.array(), equalTo(Arrays.copyOfRange(someBytes, 0, 3)));
    }

    @Test
    void testReadFullyToByteBufferWithWholeArray() throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(TEST_SIZE);
        this.stream.readFully(byteBuffer);
        assertThat(byteBuffer.array(), equalTo(someBytes));
    }

    @Test
    void testReadFullyToByteBufferEOF() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(TEST_SIZE + 1);
        assertThrows(EOFException.class, () -> this.stream.readFully(byteBuffer));
    }

    @Test
    void testReadToByteBufferWithWholeArray() throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(TEST_SIZE);
        final int readBytes = this.stream.read(byteBuffer);
        assertThat(byteBuffer.array(), equalTo(Arrays.copyOfRange(someBytes, 0, readBytes)));
    }

    @Test
    void testReadToByteBufferReachesEOF() throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(TEST_SIZE + 1);
        assertThat(this.stream.read(byteBuffer), equalTo(TEST_SIZE));
    }

    @Test
    void testReadToByteBufferWithNothingToRead() throws IOException {
        this.stream.seek(TEST_SIZE);
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        assertThat(this.stream.read(byteBuffer), equalTo(-1));
    }

    @Test
    void testSeek() throws IOException {
        this.stream.seek(3);
        assertThat((byte) this.stream.read(), equalTo(someBytes[3]));
    }

    @Test
    void testGetPos() throws IOException {
        this.stream.seek(4);
        assertThat(this.stream.getPos(), equalTo(4L));
    }

    @Test
    void testPositionIsIncrementedByRead() throws IOException {
        this.stream.read();
        assertThat(this.stream.getPos(), equalTo(1L));
    }

    @Test
    void testReadFully() throws IOException {
        final byte[] result = new byte[TEST_SIZE];
        this.stream.readFully(result);
        assertThat(result, equalTo(someBytes));
    }

    @Test
    void testReadFullyWithOffset() throws IOException {
        final byte[] result = new byte[2];
        result[0] = 123;
        this.stream.readFully(result, 1, 1);
        assertThat(result, equalTo(new byte[] { 123, someBytes[0] }));
    }

    @Test
    void testReadFullyWithEOF() {
        final byte[] result = new byte[TEST_SIZE + 1];
        assertThrows(EOFException.class, () -> this.stream.readFully(result));
    }

    @Test
    void testSkip() throws IOException {
        final long skippedBytes = this.stream.skip(2);
        assertThat(this.stream.getPos(), equalTo(skippedBytes));
    }

    @Test
    void testPositionIsIncrementedByByteBufferRead() throws IOException {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        this.stream.readFully(byteBuffer);
        assertThat(this.stream.getPos(), equalTo(3L));
    }
}