package com.exasol.adapter.document.documentnode.csv;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.NamedCsvReader;

class NamedCsvObjectNodeTest {

    private static final String CSV_CONTENT = "header1,header2\r\nfoo1,bar1\r\nfoo2,bar2";

    @Test
    void testWithHeadersCreation() {
        assertThat(create(CSV_CONTENT), instanceOf(NamedCsvObjectNode.class));
    }

    @Test
    void testWHHasKey() {
        assertThat(create(CSV_CONTENT).hasKey("header1"), equalTo(true));
    }

    @Test
    void testWHHasKey2() {
        assertThat(create(CSV_CONTENT).hasKey("header2"), equalTo(true));
    }

    @Test
    void testWHNotHasKey() {
        assertThat(create(CSV_CONTENT).hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testWHNotHasKeyString() {
        assertThat(create(CSV_CONTENT).hasKey("header3"), equalTo(false));
    }

    @Test
    void testWHGet() {
        final StringHolderNode result = (StringHolderNode) create(CSV_CONTENT).get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testWHGetKeyValueMap() {
        final NamedCsvObjectNode objectNode = (NamedCsvObjectNode) create(CSV_CONTENT);
        final Map<String, DocumentNode> map = objectNode.getKeyValueMap();
        assertThat(map, aMapWithSize(2));
        assertThat(((StringHolderNode) map.get("header1")).getValue(), equalTo("foo1"));
        assertThat(((StringHolderNode) map.get("header2")).getValue(), equalTo("bar1"));
    }

    private NamedCsvObjectNode create(final String csvContent) {
        final NamedCsvReader csvWithHeadersReader = NamedCsvReader.builder().build(csvContent);
        return new NamedCsvObjectNode(CsvValueTypeConverter.create(emptyList()),
                csvWithHeadersReader.iterator().next());
    }
}
