package com.exasol.adapter.document.documentfetcher.files.csv;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.*;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.documentpath.ObjectLookupPathSegment;
import com.exasol.adapter.document.mapping.*;

class CsvIteratorTest {
    public static final String CSV_EXAMPLE = "test-1\ntest-2";
    public static final String CSV_WITH_HEADERS_EXAMPLE = "header-1\ntest-1\ntest-2";
    public static final String CSV_WITH_DUPLICATE_HEADERS_EXAMPLE = "header,header\ntest-1a,test-1b\ntest-2a,test-2b";

    @Test
    void testReadLines() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE, List.of(varcharMapping("0")));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testReadLineContent() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE, List.of(varcharMapping("0")));
        assertThat(result.size(), equalTo(2));
        assertThat(((StringHolderNode) ((DocumentObject) result.get(0)).get("0")).getValue(), equalTo("test-1"));
        assertThat(((StringHolderNode) ((DocumentObject) result.get(1)).get("0")).getValue(), equalTo("test-2"));
    }

    @Test
    void testWithHeadersReadLines() {
        final List<DocumentNode> result = readCsvWithHeadersLines(CSV_WITH_HEADERS_EXAMPLE,
                List.of(varcharMapping("header-1")));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testWithDuplicateHeadersReadLines() {
        final List<ColumnMapping> columns = List.of(varcharMapping("header-1"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> createCsvWithHeadersIterator(CSV_WITH_DUPLICATE_HEADERS_EXAMPLE, columns));
        assertThat(exception.getMessage(), equalTo("Duplicate header field 'header' found"));
    }

    @Test
    void testReadLinesWithAdditionalNewLine() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE + "\n\n", List.of(varcharMapping("0")));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testWithHeadersReadLinesWithAdditionalNewLine() {
        final List<DocumentNode> result = readCsvWithHeadersLines(CSV_WITH_HEADERS_EXAMPLE + "\n\n",
                List.of(varcharMapping("header-1")));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testHasNextHasNoSideEffects() {
        final CsvIterator csvIterator = createCsvIterator(CSV_EXAMPLE, List.of(varcharMapping("0")));
        csvIterator.hasNext();
        csvIterator.hasNext();
        csvIterator.next();
        csvIterator.next();
        assertThat(csvIterator.hasNext(), equalTo(false));
    }

    @Test
    void testWithHeadersHasNextHasNoSideEffects() {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE,
                List.of(varcharMapping("header-1")));
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
        final CsvIterator csvIterator = createCsvIterator(CSV_EXAMPLE, List.of(varcharMapping("0")));
        csvIterator.next();
        csvIterator.next();
        assertThrows(NoSuchElementException.class, csvIterator::next);
    }

    @Test
    void testNoSuchElementWithHeadersException() {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(CSV_WITH_HEADERS_EXAMPLE,
                List.of(varcharMapping("header-1")));
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
    @CsvSource({ "ascii", "öäüÖÄÜß", "👍" })
    void testConvertStringSuccess(final String csvValue) {
        final ColumnMapping column = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(pathExpression("col")).build();
        final StringHolderNode value = convertCsvValue(csvValue, column, StringHolderNode.class);
        assertThat(value.getValue(), is(csvValue));
    }

    @ParameterizedTest
    @CsvSource({ "1.234,1.234", "42,42", "1.234e-5,0.00001234" })
    void testConvertDecimalSuccess(final String csvValue, final BigDecimal expected) {
        final ColumnMapping column = PropertyToDecimalColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final BigDecimalHolderNode value = convertCsvValue(csvValue, column, BigDecimalHolderNode.class);
        assertThat(value.getValue(), equalTo(expected));
    }

    @ParameterizedTest
    @CsvSource({ "1.234,1.234", "42,42", "1.234e-5,0.00001234" })
    void testConvertDoubleSuccess(final String csvValue, final double expected) {
        final ColumnMapping column = PropertyToDoubleColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final DoubleHolderNode value = convertCsvValue(csvValue, column, DoubleHolderNode.class);
        assertThat(value.getValue(), equalTo(expected));
    }

    private <T extends DocumentNode> T convertCsvValue(final String csvValue, final ColumnMapping column,
            final Class<T> expectedType) {
        final CsvIterator iterator = createCsvIterator(csvValue, List.of(column));
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

    private List<DocumentNode> readCsvLines(final String s, final List<ColumnMapping> csvColumns) {
        final CsvIterator csvIterator = createCsvIterator(s, csvColumns);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private List<DocumentNode> readCsvWithHeadersLines(final String s, final List<ColumnMapping> csvColumns) {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(s, csvColumns);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private PropertyToVarcharColumnMapping varcharMapping(final String columnName) {
        return PropertyToVarcharColumnMapping.builder() //
                .pathToSourceProperty(pathExpression(columnName)).build();
    }

    private CsvIterator createCsvIterator(final String content, final List<ColumnMapping> columns) {
        return createCsvIterator(content, false, columns);
    }

    private CsvIterator createCsvWithHeadersIterator(final String content, final List<ColumnMapping> csvColumns) {
        return createCsvIterator(content, true, csvColumns);
    }

    private CsvIterator createCsvIterator(final String content, final boolean withHeaders,
            final List<ColumnMapping> csvColumns) {
        return CsvIterator.create(new RemoteFile("", 0, new StringRemoteFileContent(content)), csvColumns,
                new CsvConfiguration(withHeaders));
    }
}