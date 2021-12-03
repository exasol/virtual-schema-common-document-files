package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonDocumentFetcherTest {

    @Test
    void testClosed() {
        final AssertStreamIsClosedRemoteFileContent spy = new AssertStreamIsClosedRemoteFileContent("{}");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        jsonDocumentFetcher.readDocuments(new FileSegment(new RemoteFile("", 0, spy), ENTIRE_FILE))
                .forEachRemaining(x -> {
                    // just run through
                });
        assertThat(spy.isStreamClosed(), equalTo(true));
    }

    @Test
    void testReadDocument() {
        final RemoteFile remoteFile = getRemoteFileForString("{\"id\": \"book-1\"}");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final List<DocumentNode> result = new ArrayList<>();
        jsonDocumentFetcher.readDocuments(new FileSegment(remoteFile, ENTIRE_FILE)).forEachRemaining(result::add);
        assertThat(result.size(), equalTo(1));
    }

    private RemoteFile getRemoteFileForString(final String content) {
        return new RemoteFile("string source", 0, new StringRemoteFileContent(content));
    }

    @Test
    void testReadDocumentWithSyntaxError() {
        final RemoteFile remoteFile = getRemoteFileForString("{\ninvalid syntax\n}");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final FileSegment segment = new FileSegment(remoteFile, ENTIRE_FILE);
        final InputDataException exception = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(segment));
        assertThat(exception.getMessage(), equalTo("E-VSDF-1: Error in input file 'string source'."));
    }

    @ValueSource(strings = { "", " ", "   ", "\n", "\n " })
    @ParameterizedTest
    void testReadEmptyDocument(final String emptyDocumentVariant) {
        final RemoteFile remoteFile = getRemoteFileForString(emptyDocumentVariant);
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final FileSegment segment = new FileSegment(remoteFile, ENTIRE_FILE);
        final InputDataException inputDataException = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(segment));
        assertThat(inputDataException.getMessage(), startsWith("E-VSDF-1: Error in input file 'string source'."));
    }

    @Test
    void testExceptionForSegmentedFiles() {
        final RemoteFile remoteFile = getRemoteFileForString("{}");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final FileSegment segment = new FileSegment(remoteFile, new FileSegmentDescription(2, 0));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> jsonDocumentFetcher.readDocuments(segment));
        assertThat(exception.getMessage(),
                startsWith("F-VSDF-16: The JsonDocumentFetcher does not support loading split files."));
    }
}