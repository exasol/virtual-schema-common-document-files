package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.CsvReader;

class CsvObjectNodeTest {
    private static final String CSV_CONTENT = "foo1,bar1\r\nfoo2,bar2";

    @Test
    void testCreation() {
        assertThat(create(CSV_CONTENT), instanceOf(CsvObjectNode.class));
    }

    @Test
    void testHasKey() {
        assertThat(create(CSV_CONTENT).hasKey("0"), equalTo(true));
    }

    @Test
    void testHasKey2() {
        assertThat(create(CSV_CONTENT).hasKey("1"), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        assertThat(create(CSV_CONTENT).hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testNotHasKeyString() {
        assertThat(create(CSV_CONTENT).hasKey("2"), equalTo(false));
    }

    @Test
    void testGet() {
        final StringHolderNode result = (StringHolderNode) create(CSV_CONTENT).get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetWithParentheses() {
        final CsvObjectNode node = create("\"foo1\",\"bar1\"\r\n\"foo2\",\"bar2\"");
        final StringHolderNode result = (StringHolderNode) node.get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetKeyValueMap() {
        final CsvObjectNode node = create(CSV_CONTENT);
        final Map<String, DocumentNode> map = node.getKeyValueMap();
        assertThat(map, aMapWithSize(2));
        assertThat(((StringHolderNode) map.get("0")).getValue(), equalTo("foo1"));
        assertThat(((StringHolderNode) map.get("1")).getValue(), equalTo("bar1"));
    }

    private CsvObjectNode create(final String csvContent) {
        final CsvReader csvReaderParentheses = CsvReader.builder().build(csvContent);
        return new CsvObjectNode(new CsvValueTypeConverter(), csvReaderParentheses.iterator().next());
    }
}
