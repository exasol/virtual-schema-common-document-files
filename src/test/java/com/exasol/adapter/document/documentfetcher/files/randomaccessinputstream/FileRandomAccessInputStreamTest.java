package com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;

/**
 * This class runs the tests from {@link RandomAccessInputStreamTestBase} against {@link RandomAccessFile} to see if the
 * test work on a proper implementation.
 */
class FileRandomAccessInputStreamTest extends RandomAccessInputStreamTestBase {
    @TempDir
    static Path tempDir;
    private Path testFile;

    @Override
    protected void prepareTestSetup(final byte[] testData) throws IOException {
        this.testFile = tempDir.resolve("testFile");
        Files.write(this.testFile, testData);
    }

    @Override
    protected RandomAccessInputStream getSeekableInputStream() {
        try {
            return new FileRandomAccessInputStream(this.testFile.toFile());
        } catch (final FileNotFoundException exception) {
            throw new IllegalArgumentException(exception);
        }
    }
}
