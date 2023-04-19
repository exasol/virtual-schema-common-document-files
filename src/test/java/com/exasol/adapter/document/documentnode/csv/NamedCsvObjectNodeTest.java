package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.NamedCsvReader;

class NamedCsvObjectNodeTest {

    public static final NamedCsvReader csvWithHeadersReader = NamedCsvReader.builder()
            .build("header1,header2\r\nfoo1,bar1\r\nfoo2,bar2");
    public static final DocumentNode csvWithHeadersFirstLineNode = new NamedCsvObjectNode(
            csvWithHeadersReader.iterator().next());
    public static final NamedCsvObjectNode csvWithHeadersFirstLineCsvNode = (NamedCsvObjectNode) csvWithHeadersFirstLineNode;

    @Test
    void testWithHeadersCreation() {
        assertThat(csvWithHeadersFirstLineNode, instanceOf(NamedCsvObjectNode.class));
    }

    @Test
    void testWHHasKey() {
        assertThat(csvWithHeadersFirstLineCsvNode.hasKey("header1"), equalTo(true));
    }

    @Test
    void testWHHasKey2() {
        assertThat(csvWithHeadersFirstLineCsvNode.hasKey("header2"), equalTo(true));
    }

    @Test
    void testWHNotHasKey() {
        assertThat(csvWithHeadersFirstLineCsvNode.hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testWHNotHasKeyString() {
        assertThat(csvWithHeadersFirstLineCsvNode.hasKey("header3"), equalTo(false));
    }

    @Test
    void testWHGet() {
        final StringHolderNode result = (StringHolderNode) csvWithHeadersFirstLineCsvNode.get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testWHGetKeyValueMap() {
        final NamedCsvObjectNode objectNode = (NamedCsvObjectNode) csvWithHeadersFirstLineNode;
        final StringHolderNode result = (StringHolderNode) objectNode.getKeyValueMap().get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }
}
