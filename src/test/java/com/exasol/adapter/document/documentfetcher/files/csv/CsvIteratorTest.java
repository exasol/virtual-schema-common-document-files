package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvIteratorTest {
    public static final String CSV_EXAMPLE = "test-1\ntest-2";

    @Test
    void testReadLines() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testReadLinesWithAdditionalNewLine() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE + "\n\n");
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
    void testClose() {
        final AssertStreamIsClosedRemoteFileContent assertStreamIsClosedRemoteFileContent = new AssertStreamIsClosedRemoteFileContent(
                "");
        final CsvIterator csvIterator = new CsvIterator(
                new RemoteFile("", 10, assertStreamIsClosedRemoteFileContent));
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

    private List<DocumentNode> readCsvLines(final String s) {
        final CsvIterator csvIterator = getCsvIterator(s);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private CsvIterator getCsvIterator(final String content) {
        return new CsvIterator(new RemoteFile("", 0, new StringRemoteFileContent(content)));
    }
}