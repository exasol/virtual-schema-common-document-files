package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;

import java.util.List;
import java.util.Map;

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
    void testWHHasKey() {
        assertThat(testee().hasKey("header1"), equalTo(true));
    }

    @Test
    void testWHHasKey2() {
        assertThat(testee().hasKey("header2"), equalTo(true));
    }

    @Test
    void testWHNotHasKey() {
        assertThat(testee().hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testWHNotHasKeyString() {
        assertThat(testee().hasKey("header3"), equalTo(false));
    }

    @Test
    void testWHGet() {
        final StringHolderNode result = (StringHolderNode) testee().get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testWHGetKeyValueMap() {
        final NamedCsvObjectNode objectNode = (NamedCsvObjectNode) testee();
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
        return new NamedCsvObjectNode(CsvValueTypeConverter.create(csvColumns), csvWithHeadersReader.iterator().next());
    }

    private ColumnMapping varcharCol(final String columnName) {
        return PropertyToVarcharColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addPathSegment(new ObjectLookupPathSegment(segment)).build();
    }
}
