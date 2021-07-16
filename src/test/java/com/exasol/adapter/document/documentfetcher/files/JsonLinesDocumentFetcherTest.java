package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonLinesDocumentFetcherTest {

    @Test
    void testReadDocuments() {
        final JsonLinesDocumentFetcher documentFetcher = new JsonLinesDocumentFetcher(null, null, null);
        final LoadedFile loadedFile = new StringLoadedFile("{\"id\": \"book-1\"}\n{\"id\": \"book-2\"}",
                "string source");
        final List<DocumentNode> result = new ArrayList<>();
        documentFetcher.readDocuments(loadedFile).forEachRemaining(result::add);
        assertThat(result.size(), equalTo(2));
    }
}