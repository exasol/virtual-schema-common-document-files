package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.documentpath.ObjectLookupPathSegment;
import com.exasol.adapter.document.mapping.ColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;

import de.siegmar.fastcsv.reader.NamedCsvReader;

class NamedCsvObjectNodeTest {

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

    @Test
    void testGet() {
        final StringHolderNode result = (StringHolderNode) testee().get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetUnknownKey() {
        final NamedCsvObjectNode testee = testee();
        final NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> testee.get("unknown"));
        assertThat(exception.getMessage(),
                equalTo("No element with name 'unknown' found. Valid names are: [header1, header2]"));
    }

    @Test
    void testGetKeyValueMap() {
        final NamedCsvObjectNode objectNode = testee();
        final Map<String, DocumentNode> map = objectNode.getKeyValueMap();
        assertThat(map, aMapWithSize(2));
        assertThat(((StringHolderNode) map.get("header1")).getValue(), equalTo("foo1"));
        assertThat(((StringHolderNode) map.get("header2")).getValue(), equalTo("bar1"));
    }

    private NamedCsvObjectNode testee() {
        return create("header1,header2\r\nfoo1,bar1\r\nfoo2,bar2",
                List.of(varcharCol("header1"), varcharCol("header2")));
    }

    private NamedCsvObjectNode create(final String csvContent, final List<ColumnMapping> csvColumns) {
        final NamedCsvReader csvWithHeadersReader = NamedCsvReader.builder().build(csvContent);
        return new NamedCsvObjectNode("resourceName", CsvValueTypeConverter.create(csvColumns),
                csvWithHeadersReader.iterator().next());
    }

    private ColumnMapping varcharCol(final String columnName) {
        return PropertyToVarcharColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addPathSegment(new ObjectLookupPathSegment(segment)).build();
    }
}
