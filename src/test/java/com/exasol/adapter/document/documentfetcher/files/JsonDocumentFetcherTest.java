package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonDocumentFetcherTest {

    @Test
    void testClosed() {
        final AssertStreamIsClosedLoadedFile remoteFile = new AssertStreamIsClosedLoadedFile("{}");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        jsonDocumentFetcher.readDocuments(remoteFile).forEachRemaining(x -> {
            // just run through
        });
        assertThat(remoteFile.isStreamClosed(), equalTo(true));
    }

    @Test
    void testReadDocument() {
        final RemoteFile remoteFile = new StringLoadedFile("{\"id\": \"book-1\"}", "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final List<DocumentNode> result = new ArrayList<>();
        jsonDocumentFetcher.readDocuments(remoteFile).forEachRemaining(result::add);
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void testReadDocumentWithSyntaxError() {
        final RemoteFile remoteFile = new StringLoadedFile("{\ninvalid syntax\n}", "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final InputDataException exception = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(remoteFile));
        assertThat(exception.getMessage(), equalTo("E-VSDF-1: Error in input file 'string source'."));
    }

    @ValueSource(strings = { "", " ", "   ", "\n", "\n " })
    @ParameterizedTest
    void testReadEmptyDocument(final String emptyDocumentVariant) {
        final RemoteFile remoteFile = new StringLoadedFile(emptyDocumentVariant, "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher();
        final InputDataException inputDataException = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(remoteFile));
        assertThat(inputDataException.getMessage(), startsWith("E-VSDF-1: Error in input file 'string source'."));
    }
}