package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvDocumentFetcherTest {
    @Test
    void testReadDocuments() {
        final CsvDocumentFetcher documentFetcher = new CsvDocumentFetcher();
        final RemoteFile remoteFile = new RemoteFile("", 0,
                new StringRemoteFileContent("book-1\nbook-2"));
        final List<DocumentNode> result = new ArrayList<>();
        documentFetcher.readDocuments(new FileSegment(remoteFile, ENTIRE_FILE)).forEachRemaining(result::add);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testExceptionOnSegmentedFile() {
        final CsvDocumentFetcher documentFetcher = new CsvDocumentFetcher();
        final RemoteFile remoteFile = new RemoteFile("", 0, new StringRemoteFileContent("{}"));
        final FileSegment segment = new FileSegment(remoteFile, new FileSegmentDescription(2, 0));
        final IllegalStateException exception = Assert.assertThrows(IllegalStateException.class,
                () -> documentFetcher.readDocuments(segment));
        assertThat(exception.getMessage(), startsWith("F-VSDF-17"));
    }
}