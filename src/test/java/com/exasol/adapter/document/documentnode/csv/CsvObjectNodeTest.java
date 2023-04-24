package com.exasol.adapter.document.documentnode.csv;

import static com.exasol.adapter.document.documentnode.util.DocumentNodeMatchers.stringNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueTypeConverterRegistry;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.mapping.*;

import de.siegmar.fastcsv.reader.CsvReader;

class CsvObjectNodeTest {

    @Test
    void testCreation() {
        assertThat(testee(), instanceOf(CsvObjectNode.class));
    }

    @Test
    void testHasKey() {
        assertThat(testee().hasKey("0"), equalTo(true));
    }

    @Test
    void testHasKey2() {
        assertThat(testee().hasKey("1"), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        assertThat(testee().hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testNotHasKeyString() {
        assertThat(testee().hasKey("2"), equalTo(false));
    }

    @Test
    void testGet() {
        assertThat(testee().get("0"), stringNode("foo1"));
    }

    @Test
    void testGetMissingElement() {
        final DocumentObject testee = testee();
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> testee.get("2"));
        assertThat(exception.getMessage(), equalTo("No element with name '2' found."));
    }

    @Test
    void testGetMissingElementWithStringKey() {
        final DocumentObject testee = testee();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee.get("unknown"));
        assertThat(exception.getMessage(), equalTo("No element with name 'unknown' found."));
    }

    @Test
    void testGetWithParentheses() {
        final DocumentObject node = create("\"foo1\",\"bar1\"\r\n\"foo2\",\"bar2\"",
                List.of(varcharCol("0"), varcharCol("1")));
        assertThat(node.get("0"), stringNode("foo1"));
    }

    @Test
    void testGetConversionFails() {
        final DocumentObject testee = create("not-a-number", List.of(decimalCol("0")));
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee.get("0"));
        assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'not-a-number' using converter .* \\(file 'resourceName', row 1, column 0\\).*"));
    }

    @Test
    void testGetKeyValueMap() {
        final Map<String, DocumentNode> map = testee().getKeyValueMap();
        assertAll(() -> assertThat(map, aMapWithSize(2)), () -> assertThat(map.get("0"), stringNode("foo1")),
                () -> assertThat(map.get("1"), stringNode("bar1")));
    }

    @Test
    void testGetKeyValueMapConversionFails() {
        final DocumentObject testee = create("not-a-number", List.of(decimalCol("0")));
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, testee::getKeyValueMap);
        assertThat(exception.getMessage(), matchesPattern(
                "E-VSDF-66: Error converting value 'not-a-number' using converter .* \\(file 'resourceName', row 1, column 0\\).*"));
    }

    private DocumentObject testee() {
        return create("foo1,bar1\r\nfoo2,bar2", List.of(varcharCol("0"), varcharCol("1")));
    }

    private DocumentObject create(final String csvContent, final List<ColumnMapping> csvColumns) {
        final CsvReader csvReaderParentheses = CsvReader.builder().build(csvContent);
        return new CsvObjectNode("resourceName", CsvValueTypeConverterRegistry.create(csvColumns),
                csvReaderParentheses.iterator().next());
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
