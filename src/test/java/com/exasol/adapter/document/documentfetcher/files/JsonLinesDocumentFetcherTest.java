package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

class JsonLinesDocumentFetcherTest {

    @Test
    void testReadDocuments() {
        final JsonLinesDocumentFetcher documentFetcher = new JsonLinesDocumentFetcher(null, null, null);
        final InputStreamWithResourceName loadedFile = new InputStreamWithResourceName(
                new ByteArrayInputStream("{\"id\": \"book-1\"}\n{\"id\": \"book-2\"}".getBytes()), "string source");
        final Stream<DocumentNode<JsonNodeVisitor>> documentNodeStream = documentFetcher.readDocuments(loadedFile);
        assertThat(documentNodeStream.count(), equalTo(2L));
    }
}