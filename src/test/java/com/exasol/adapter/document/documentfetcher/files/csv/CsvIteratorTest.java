package com.exasol.adapter.document.documentfetcher.files.csv;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.BooleanHolderNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.documentpath.ObjectLookupPathSegment;
import com.exasol.adapter.document.mapping.*;

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
                () -> createCsvWithHeadersIterator(CSV_WITH_DUPLICATE_HEADERS_EXAMPLE));
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
        final CsvIterator csvIterator = createCsvIterator(CSV_EXAMPLE);
        csvIterator.hasNext();
        csvIterator.hasNext();
        csvIterator.next();
        csvIterator.next();
        assertThat(csvIterator.hasNext(), equalTo(false));
    }

    @Test
    void testWithHeadersHasNextHasNoSideEffects() {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE);
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
                new RemoteFile("", 10, assertStreamIsClosedRemoteFileContent), emptyList(),
                new CsvConfiguration(false));
        csvIterator.close();
        assertThat(assertStreamIsClosedRemoteFileContent.isStreamClosed(), equalTo(true));
    }

    @Test
    void testWithHeadersClose() {
        final AssertStreamIsClosedRemoteFileContent assertStreamIsClosedRemoteFileContent = new AssertStreamIsClosedRemoteFileContent(
                "");
        final CsvIterator csvIterator = CsvIterator.create(
                new RemoteFile("", 10, assertStreamIsClosedRemoteFileContent), emptyList(), new CsvConfiguration(true));
        csvIterator.close();
        assertThat(assertStreamIsClosedRemoteFileContent.isStreamClosed(), equalTo(true));
    }

    @Test
    void testNoSuchElementException() {
        final CsvIterator csvIterator = createCsvIterator(CSV_EXAMPLE);
        csvIterator.next();
        csvIterator.next();
        assertThrows(NoSuchElementException.class, csvIterator::next);
    }

    @Test
    void testNoSuchElementWithHeadersException() {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE);
        csvIterator.next();
        csvIterator.next();
        assertThrows(NoSuchElementException.class, csvIterator::next);
    }

    @ParameterizedTest
    @CsvSource({ "true,true", "TRUE,true", "True,true", "false,false", "FALSE,false", "False,false" })
    void testConvertBooleanSuccess(final String csvValue, final boolean expected) {
        final ColumnMapping column = PropertyToBoolColumnMapping.builder().pathToSourceProperty(pathExpression("col"))
                .build();
        final BooleanHolderNode value = convertCsvValue(csvValue, column, BooleanHolderNode.class);
        assertThat(value.getValue(), is(expected));
    }

    @ParameterizedTest
    @CsvSource({ "asci", "öäüÖÄÜß", "👍" })
    void testConvertStringSuccess(final String csvValue) {
        final ColumnMapping column = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(pathExpression("col")).build();
        final StringHolderNode value = convertCsvValue(csvValue, column, StringHolderNode.class);
        assertThat(value.getValue(), is(csvValue));
    }

    private <T extends DocumentNode> T convertCsvValue(final String csvValue, final ColumnMapping column,
            final Class<T> expectedType) {
        final CsvIterator iterator = createCsvIterator(csvValue, column);
        final DocumentNode firstRow = iterator.next();
        assertThat(firstRow, instanceOf(DocumentObject.class));
        final DocumentObject object = (DocumentObject) firstRow;
        final DocumentNode value = object.get("0");
        assertThat(value, instanceOf(expectedType));
        return expectedType.cast(value);
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addPathSegment(new ObjectLookupPathSegment(segment)).build();
    }

    private List<DocumentNode> readCsvLines(final String s) {
        final CsvIterator csvIterator = createCsvIterator(s);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private List<DocumentNode> readCsvWithHeadersLines(final String s) {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(s);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private CsvIterator createCsvIterator(final String content) {
        return createCsvIterator(content, false, emptyList());
    }

    private CsvIterator createCsvIterator(final String content, final ColumnMapping column) {
        return createCsvIterator(content, false, List.of(column));
    }

    private CsvIterator createCsvWithHeadersIterator(final String content) {
        return createCsvIterator(content, true, emptyList());
    }

    private CsvIterator createCsvIterator(final String content, final boolean withHeaders,
            final List<ColumnMapping> csvColumns) {
        return CsvIterator.create(new RemoteFile("", 0, new StringRemoteFileContent(content)), csvColumns,
                new CsvConfiguration(withHeaders));
    }
}