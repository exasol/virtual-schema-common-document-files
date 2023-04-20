package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.documentpath.ObjectLookupPathSegment;
import com.exasol.adapter.document.mapping.ColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;

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
        final StringHolderNode result = (StringHolderNode) testee().get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetMissingElement() {
        final CsvObjectNode testee = testee();
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> testee.get("2"));
        assertThat(exception.getMessage(), equalTo("No element with name '2' found."));
    }

    @Test
    void testGetMissingElementWithStringKey() {
        final CsvObjectNode testee = testee();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee.get("unknown"));
        assertThat(exception.getMessage(), equalTo("No element with name 'unknown' found."));
    }

    @Test
    void testGetWithParentheses() {
        final CsvObjectNode node = create("\"foo1\",\"bar1\"\r\n\"foo2\",\"bar2\"",
                List.of(varcharCol("0"), varcharCol("1")));
        final StringHolderNode result = (StringHolderNode) node.get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetKeyValueMap() {
        final Map<String, DocumentNode> map = testee().getKeyValueMap();
        assertAll(() -> assertThat(map, aMapWithSize(2)),
                () -> assertThat(((StringHolderNode) map.get("0")).getValue(), equalTo("foo1")),
                () -> assertThat(((StringHolderNode) map.get("1")).getValue(), equalTo("bar1")));
    }

    private CsvObjectNode testee() {
        return create("foo1,bar1\r\nfoo2,bar2", List.of(varcharCol("0"), varcharCol("1")));
    }

    private CsvObjectNode create(final String csvContent, final List<ColumnMapping> csvColumns) {
        final CsvReader csvReaderParentheses = CsvReader.builder().build(csvContent);
        return new CsvObjectNode("resourceName", CsvValueTypeConverter.create(csvColumns),
                csvReaderParentheses.iterator().next());
    }

    private ColumnMapping varcharCol(final String columnName) {
        return PropertyToVarcharColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addPathSegment(new ObjectLookupPathSegment(segment)).build();
    }
}
