package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.testutil.DocumentNodeMatchers.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.mapping.*;

import de.siegmar.fastcsv.reader.CsvParseException;

class CsvIteratorTest {
    public static final String CSV_EXAMPLE = "test-1\ntest-2";
    public static final String CSV_WITH_HEADERS_EXAMPLE = "header-1\ntest-1\ntest-2";
    public static final String CSV_WITH_DUPLICATE_HEADERS_EXAMPLE = "header,header\ntest-1a,test-1b\ntest-2a,test-2b";

    enum CsvMode {
        WITH_HEADER, WITHOUT_HEADER
    }

    @Test
    void testReadLines() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE, List.of(varcharMapping("0")));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testReadLineContent() {
        final List<DocumentNode> result = readCsvLines(CSV_EXAMPLE, List.of(varcharMapping("0")));
        assertThat(result.size(), equalTo(2));
        assertThat(((DocumentObject) result.get(0)).get("0"), stringNode("test-1"));
        assertThat(((DocumentObject) result.get(1)).get("0"), stringNode("test-2"));
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
        final CsvIterator iterator = createCsvWithHeadersIterator(CSV_WITH_DUPLICATE_HEADERS_EXAMPLE, columns);
        final CsvParseException exception = assertThrows(CsvParseException.class, iterator::next);
        assertAll(() -> assertThat(exception.getMessage(), equalTo("Exception when reading first record")),
                () -> assertThat(exception.getCause().getMessage(), equalTo(
                        "E-VSDF-72: Duplicate field 'header' at line number 1 / field index 1, all fields: ['header']")));
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
    @CsvSource({ "true,true", "true,true", "TRUE,true", "True,true", "false,false", "FALSE,false", "False,false" })
    void testConvertBooleanSuccess(final String csvValue, final boolean expected) {
        final ColumnMapping column = PropertyToBoolColumnMapping.builder().pathToSourceProperty(pathExpression("col"))
                .build();
        assertThat(convertCsvValue(csvValue, column), booleanNode(expected));
    }

    @Test
    void testConvertBooleanFailure() {
        final ColumnMapping column = PropertyToBoolColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("invalid-bool", column));
        assertAll(() -> assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'invalid-bool' using converter .* \\(file '', row 1, column 0\\).*")),
                () -> assertThat(exception.getCause().getMessage(), equalTo(
                        "E-VSDF-65: Value 'invalid-bool' is not a boolean value. Please use only values 'true' and 'false' (case insensitive).")));
    }

    @ParameterizedTest
    @CsvSource({ "ascii", "öäüÖÄÜß", "👍", "#+±~$%", "true", "false", "123", "123.456" })
    void testConvertStringSuccess(final String csvValue) {
        assertThat(convertCsvValue(csvValue, varcharMapping("col")), stringNode(csvValue));
    }

    @ParameterizedTest
    @CsvSource({ "''", "' '", " val", "val ", " val " })
    void testConvertStringEmptyValue(final String value) {
        final List<DocumentNode> rows = readCsvLines("val1," + value + ",val3",
                List.of(varcharMapping("0"), varcharMapping("1"), varcharMapping("2")));
        assertThat(rows, hasSize(1));
        final DocumentObject firstRow = (DocumentObject) rows.get(0);
        assertAll( //
                () -> assertThat(firstRow.get("0"), stringNode("val1")),
                () -> assertThat(firstRow.get("1"), stringNode(value)),
                () -> assertThat(firstRow.get("2"), stringNode("val3")));
    }

    @ParameterizedTest
    @CsvSource({ "''", "' '", " val", "val ", " val " })
    void testConvertStringEmptyValueWithHeader(final String value) {
        final List<DocumentNode> rows = readCsvWithHeadersLines("col1,col2,col3\nval1," + value + ",val3",
                List.of(varcharMapping("col1"), varcharMapping("col2"), varcharMapping("col3")));
        assertThat(rows, hasSize(1));
        final DocumentObject firstRow = (DocumentObject) rows.get(0);
        assertAll( //
                () -> assertThat(firstRow.get("col1"), stringNode("val1")),
                () -> assertThat(firstRow.get("col2"), stringNode(value)),
                () -> assertThat(firstRow.get("col3"), stringNode("val3")));
    }

    @ParameterizedTest
    @CsvSource({ "''", "' '", " val", "val ", " val " })
    void testConvertStringQuotesRemoved(final String value) {
        final List<DocumentNode> rows = readCsvWithHeadersLines("col1,col2,col3\nval1,\"" + value + "\",val3",
                List.of(varcharMapping("col1"), varcharMapping("col2"), varcharMapping("col3")));
        assertThat(rows, hasSize(1));
        final DocumentObject firstRow = (DocumentObject) rows.get(0);
        assertAll( //
                () -> assertThat(firstRow.get("col1"), stringNode("val1")),
                () -> assertThat(firstRow.get("col2"), stringNode(value)),
                () -> assertThat(firstRow.get("col3"), stringNode("val3")));
    }

    @ParameterizedTest
    @CsvSource({ "col1", " col1", "col1 ", " col1 ", "\tcol1\t" })
    void testConvertHeaderWithWhitespace(final String columnName) {
        final List<DocumentNode> rows = readCsvWithHeadersLines(columnName + ",col2\nval1,val2",
                List.of(varcharMapping("col1"), varcharMapping("col2")));
        assertThat(rows, hasSize(1));
        final DocumentObject firstRow = (DocumentObject) rows.get(0);
        assertAll( //
                () -> assertThat(firstRow.get("col1"), stringNode("val1")),
                () -> assertThat(firstRow.get("col2"), stringNode("val2")));
    }

    @ParameterizedTest
    @CsvSource({ "1.234,1.234", "42,42", "1.234e-5,0.00001234" })
    void testConvertDecimalSuccess(final String csvValue, final BigDecimal expected) {
        final ColumnMapping column = PropertyToDecimalColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        assertThat(convertCsvValue(csvValue, column), decimalNode(expected));
    }

    @Test
    void testConvertDecimalFailure() {
        final ColumnMapping column = PropertyToDecimalColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("invalid-decimal", column));
        assertAll(() -> assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'invalid-decimal' using converter .* \\(file '', row 1, column 0\\).*")),
                () -> assertThat(exception.getCause().getMessage(), equalTo(
                        "Character i is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.")));
    }

    @ParameterizedTest
    @CsvSource({ "1.234,1.234", "42,42", "1.234e-5,0.00001234" })
    void testConvertDoubleSuccess(final String csvValue, final double expected) {
        final ColumnMapping column = PropertyToDoubleColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        assertThat(convertCsvValue(csvValue, column), doubleNode(expected));
    }

    @Test
    void testConvertDoubleFailure() {
        final ColumnMapping column = PropertyToDoubleColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("invalid-double", column));
        assertAll(() -> assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'invalid-double' using converter .* \\(file '', row 1, column 0\\).*")),
                () -> assertThat(exception.getCause().getMessage(), equalTo("For input string: \"invalid-double\"")));
    }

    @Test
    void testConvertDateSuccess() {
        final ColumnMapping column = PropertyToDateColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        assertThat(convertCsvValue("2023-04-21", column), dateNode(java.sql.Date.valueOf("2023-04-21")));
    }

    @Test
    void testConvertDateFailure() {
        final ColumnMapping column = PropertyToDateColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("invalid-date", column));
        assertAll(() -> assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'invalid-date' using converter .* \\(file '', row 1, column 0\\).*")),
                () -> assertThat(exception.getCause().getMessage(), equalTo(null)));
    }

    @Test
    void testConvertTimestampSuccess() {
        final ColumnMapping column = PropertyToTimestampColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        assertThat(convertCsvValue("2023-04-21 08:41:42.123456", column),
                timestampNode(Timestamp.valueOf("2023-04-21 08:41:42.123456")));
    }

    @Test
    void testConvertTimestampFailure() {
        final ColumnMapping column = PropertyToTimestampColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("invalid-timestamp", column));
        assertAll(() -> assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'invalid-timestamp' using converter .* \\(file '', row 1, column 0\\).*")),
                () -> assertThat(exception.getCause().getMessage(),
                        equalTo("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]")));
    }

    @Test
    void testConvertUnknownColumnMappingFails() {
        final ColumnMapping column = PropertyToJsonColumnMapping.builder() //
                .pathToSourceProperty(pathExpression("col")).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> convertCsvValue("json", column));
        assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-62: Column mapping of type 'com.exasol.adapter.document.mapping.PropertyToJsonColumnMapping' .* is not supported. Please use only supported column mappings.*"));
    }

    private DocumentNode convertCsvValue(final String csvValue, final ColumnMapping column) {
        final CsvIterator iterator = createCsvIterator(CsvMode.WITHOUT_HEADER, csvValue, List.of(column));
        final DocumentNode firstRow = iterator.next();
        assertThat(firstRow, instanceOf(DocumentObject.class));
        return ((DocumentObject) firstRow).get("0");
    }

    private PropertyToVarcharColumnMapping varcharMapping(final String columnName) {
        return PropertyToVarcharColumnMapping.builder() //
                .pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addObjectLookup(segment).build();
    }

    private List<DocumentNode> readCsvLines(final String csvContent, final List<ColumnMapping> csvColumns) {
        final CsvIterator csvIterator = createCsvIterator(csvContent, csvColumns);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private List<DocumentNode> readCsvWithHeadersLines(final String csvContent, final List<ColumnMapping> csvColumns) {
        final CsvIterator csvIterator = createCsvWithHeadersIterator(csvContent, csvColumns);
        final List<DocumentNode> result = new ArrayList<>();
        csvIterator.forEachRemaining(result::add);
        return result;
    }

    private CsvIterator createCsvIterator(final String content, final List<ColumnMapping> columns) {
        return createCsvIterator(CsvMode.WITHOUT_HEADER, content, columns);
    }

    private CsvIterator createCsvWithHeadersIterator(final String content, final List<ColumnMapping> csvColumns) {
        return createCsvIterator(CsvMode.WITH_HEADER, content, csvColumns);
    }

    private CsvIterator createCsvIterator(final CsvMode mode, final String content,
            final List<ColumnMapping> csvColumns) {
        return CsvIterator.create(new RemoteFile("", 0, new StringRemoteFileContent(content)), csvColumns,
                new CsvConfiguration(mode == CsvMode.WITH_HEADER));
    }
}
