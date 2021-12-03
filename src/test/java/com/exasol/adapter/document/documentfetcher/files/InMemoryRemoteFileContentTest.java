package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class InMemoryRemoteFileContentTest {
    private static final byte[] TEST_BYTES = "test".getBytes(StandardCharsets.UTF_8);
    private static final InMemoryRemoteFileContent REMOTE_FILE_CONTENT = new InMemoryRemoteFileContent(TEST_BYTES);

    @Test
    void testGetInputStream() throws IOException {
        assertThat(REMOTE_FILE_CONTENT.getInputStream().readAllBytes(), equalTo(TEST_BYTES));
    }

    @Test
    void testGetRandomAccessInputStream() throws IOException {
        assertThat(REMOTE_FILE_CONTENT.getRandomAccessInputStream().readAllBytes(), equalTo(TEST_BYTES));
    }

    @Test
    void testGetExceptionOnLoadAssync() {
        assertThrows(UnsupportedOperationException.class, REMOTE_FILE_CONTENT::loadAsync);
    }
}