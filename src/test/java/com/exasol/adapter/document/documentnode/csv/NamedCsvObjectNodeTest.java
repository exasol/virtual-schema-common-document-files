package com.exasol.adapter.document.documentnode.csv;

import static com.exasol.adapter.document.documentnode.util.DocumentNodeMatchers.stringNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueConverters;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.mapping.*;

import de.siegmar.fastcsv.reader.NamedCsvReader;

class NamedCsvObjectNodeTest {

    @Test
    void testCreateFailsForDuplicateHeadersWithoutSpaces() {
        final List<ColumnMapping> columns = List.of(varcharCol("col"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> create("col,col\nval1,val2", columns));
        assertThat(exception.getMessage(), equalTo("Duplicate header field 'col' found"));
    }

    @Test
    void testCreateFailsForDuplicateHeaders() {
        final List<ColumnMapping> columns = List.of(varcharCol("col"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> create("col, col\nval1,val2", columns));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-69: CSV file 'resourceName' contains headers with duplicate names: ['col', ' col']. Ensure that the headers are unique."));
    }

    @Test
    void testWithHeadersCreation() {
        assertThat(testee(), instanceOf(NamedCsvObjectNode.class));
    }

    @Test
    void testHasKey() {
        assertThat(testee().hasKey("header1"), equalTo(true));
    }

    @Test
    void testHasKey2() {
        assertThat(testee().hasKey("header2"), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        assertThat(testee().hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testNotHasKeyString() {
        assertThat(testee().hasKey("header3"), equalTo(false));
    }

    @ParameterizedTest
    @CsvSource({ "header2", "' header2'", "'header2 '", "'\theader2'", "'header2\t'", "' header2 '" })
    void testNotHasKeyTrimsSpaces(final String header) {
        final DocumentObject testee = create("header1," + header + ",header3\nfoo1,bar1,foobar",
                List.of(varcharCol("header1"), varcharCol("header2"), varcharCol("header3")));
        assertThat("Header '" + header + "' not found", testee.hasKey("header2"), equalTo(true));
    }

    @Test
    void testGet() {
        assertThat(testee().get("header1"), stringNode("foo1"));
    }

    @Test
    void testGetUnknownKey() {
        final DocumentObject testee = testee();
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> testee.get("unknown"));
        assertThat(exception.getMessage(),
                equalTo("No element with name 'unknown' found. Valid names are: [header1, header2]"));
    }

    @ParameterizedTest
    @CsvSource({ "header2", "' header2'", "'header2 '", "'\theader2'", "'header2\t'", "' header2 '" })
    void testGetWithSpaces(final String header) {
        final DocumentObject testee = create("header1," + header + ",header3\nfoo1,bar1,foobar",
                List.of(varcharCol("header1"), varcharCol("header2"), varcharCol("header3")));
        assertThat(testee.get("header2"), stringNode("bar1"));
    }

    @Test
    void testGetConversionFails() {
        final DocumentObject testee = create("col\nnot-a-number", List.of(decimalCol("col")));
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee.get("col"));
        assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-67: Error converting value 'not-a-number' using converter .* \\(file 'resourceName', row 2, column 'col'\\).*"));
    }

    @Test
    void testGetKeyValueMap() {
        final DocumentObject objectNode = testee();
        final Map<String, DocumentNode> map = objectNode.getKeyValueMap();
        assertThat(map, aMapWithSize(2));
        assertThat(map.get("header1"), stringNode("foo1"));
        assertThat(map.get("header2"), stringNode("bar1"));
    }

    @Test
    void testGetKeyValueMapConversionFails() {
        final DocumentObject testee = create("col\nnot-a-number", List.of(decimalCol("col")));
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, testee::getKeyValueMap);
        assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-67: Error converting value 'not-a-number' using converter .* \\(file 'resourceName', row 2, column 'col'\\).*"));
    }

    @ParameterizedTest
    @CsvSource({ "header2", "' header2'", "'header2 '", "'\theader2'", "'header2\t'", "' header2 '" })
    void testGetKeyValueMapWithSpaces(final String header) {
        final DocumentObject testee = create("header1," + header + ",header3\nfoo1,bar1,foobar",
                List.of(varcharCol("header1"), varcharCol("header2"), varcharCol("header3")));
        assertThat(testee.getKeyValueMap().get("header2"), stringNode("bar1"));
    }

    private DocumentObject testee() {
        return create("header1,header2\r\nfoo1,bar1\r\nfoo2,bar2",
                List.of(varcharCol("header1"), varcharCol("header2")));
    }

    private NamedCsvObjectNode create(final String csvContent, final List<ColumnMapping> csvColumns) {
        final NamedCsvReader csvWithHeadersReader = NamedCsvReader.builder().build(csvContent);
        return new NamedCsvObjectNode("resourceName", CsvValueConverters.create(csvColumns),
                csvWithHeadersReader.iterator().next());
    }

    private ColumnMapping varcharCol(final String columnName) {
        return PropertyToVarcharColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping decimalCol(final String columnName) {
        return PropertyToDecimalColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addObjectLookup(segment).build();
    }
}
