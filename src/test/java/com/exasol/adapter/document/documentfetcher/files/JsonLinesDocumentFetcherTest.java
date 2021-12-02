package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonLinesDocumentFetcherTest {

    @Test
    void testReadDocuments() {
        final JsonLinesDocumentFetcher documentFetcher = new JsonLinesDocumentFetcher();
        final RemoteFile remoteFile = new RemoteFile("", 0,
                new StringRemoteFileContent("{\"id\": \"book-1\"}\n{\"id\": \"book-2\"}"));
        final List<DocumentNode> result = new ArrayList<>();
        documentFetcher.readDocuments(new FileSegment(remoteFile, ENTIRE_FILE)).forEachRemaining(result::add);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testExceptionOnSegmentedFile() {
        final JsonLinesDocumentFetcher documentFetcher = new JsonLinesDocumentFetcher();
        final RemoteFile remoteFile = new RemoteFile("", 0, new StringRemoteFileContent("{}"));
        final FileSegment segment = new FileSegment(remoteFile, new FileSegmentDescription(2, 0));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> documentFetcher.readDocuments(segment));
        assertThat(exception.getMessage(), startsWith("F-VSDF-17"));
    }
}