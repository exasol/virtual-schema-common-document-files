package com.exasol.adapter.document.documentfetcher.files.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;

class CsvIteratorTest {
    public static final String CSV_EXAMPLE = "test-1\ntest-2";
    public static final String CSV_WITH_HEADERS_EXAMPLE = "header-1\ntest-1\ntest-2";
    public static final String CSV_WITH_DUPLICATE_HEADERS_EXAMPLE = "header,header\ntest-1a,test-1b\ntest-2a,test-2b";

    @Test
    void testReadLines() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testWithHeadersReadLines() {
        final List<DocumentNode> result = readCsvWithHeadersLines(CSV_WITH_HEADERS_EXAMPLE);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testWithDuplicateHeadersReadLines() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> getCsvWithHeadersIterator(CSV_WITH_DUPLICATE_HEADERS_EXAMPLE));
        assertThat(exception.getMessage(), equalTo("Duplicate header field 'header' found"));
    }

    @Test
    void testReadLinesWithAdditionalNewLine() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE + "\n\n");
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testWithHeadersReadLinesWithAdditionalNewLine() {
        final List<DocumentNode> result = readCsvWithHeadersLines(CSV_WITH_HEADERS_EXAMPLE + "\n\n");
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testHasNextHasNoSideEffects() {
        final CsvIterator csvIterator = getCsvIterator(CSV_EXAMPLE);
        csvIterator.hasNext();
        csvIterator.hasNext();
        csvIterator.next();
        csvIterator.next();
        assertThat(csvIterator.hasNext(), equalTo(false));
    }

    @Test
    void testWithHeadersHasNextHasNoSideEffects() {
        final CsvIterator csvIterator = getCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE);
        csvIterator.hasNext();
        csvIterator.hasNext();
        csvIterator.next();
        csvIterator.next();
        assertThat(csvIterator.hasNext(), equalTo(false));
    }

    @Test
    void testClose() {
        final AssertStreamIsClosedRemoteFileContent assertStreamIsClosedRemoteFileContent = new AssertStreamIsClosedRemoteFileContent(
                "");
        final CsvIterator csvIterator = CsvIterator.create(
                new RemoteFile("", 10, assertStreamIsClosedRemoteFileContent), null, new CsvConfiguration(false));
        csvIterator.close();
        assertThat(assertStreamIsClosedRemoteFileContent.isStreamClosed(), equalTo(true));
    }

    @Test
    void testWithHeadersClose() {
        final AssertStreamIsClosedRemoteFileContent assertStreamIsClosedRemoteFileContent = new AssertStreamIsClosedRemoteFileContent(
                "");
        final CsvIterator csvIterator = CsvIterator.create(
                new RemoteFile("", 10, assertStreamIsClosedRemoteFileContent), null, new CsvConfiguration(true));
        csvIterator.close();
        assertThat(assertStreamIsClosedRemoteFileContent.isStreamClosed(), equalTo(true));
    }

    @Test
    void testNoSuchElementException() {
        final CsvIterator csvIterator = getCsvIterator(CSV_EXAMPLE);
        csvIterator.next();
        csvIterator.next();
        assertThrows(NoSuchElementException.class, csvIterator::next);
    }

    @Test
    void testNoSuchElementWithHeadersException() {
        final CsvIterator csvIterator = getCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE);
        csvIterator.next();
        csvIterator.next();
        assertThrows(NoSuchElementException.class, csvIterator::next);
    }

    private List<DocumentNode> readCsvLines(final String s) {
        final CsvIterator csvIterator = getCsvIterator(s);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private List<DocumentNode> readCsvWithHeadersLines(final String s) {
        final CsvIterator csvIterator = getCsvWithHeadersIterator(s);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private CsvIterator getCsvIterator(final String content) {
        return CsvIterator.create(new RemoteFile("", 0, new StringRemoteFileContent(content)), null,
                new CsvConfiguration(false));
    }

    private CsvIterator getCsvWithHeadersIterator(final String content) {
        return CsvIterator.create(new RemoteFile("", 0, new StringRemoteFileContent(content)), null,
                new CsvConfiguration(true));
    }
}