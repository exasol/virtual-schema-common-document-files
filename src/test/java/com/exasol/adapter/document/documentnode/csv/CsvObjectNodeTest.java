package com.exasol.adapter.document.documentnode.csv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.CsvReader;

class CsvObjectNodeTest {
    public static final CsvReader csvReader = CsvReader.builder().build("foo1,bar1\r\nfoo2,bar2");
    public static final DocumentNode firstLineNode = new CsvObjectNode(csvReader.iterator().next());
    public static final CsvObjectNode firstLineCsvNode = (CsvObjectNode) firstLineNode;

    @Test
    void testCreation() {
        assertThat(firstLineNode, instanceOf(CsvObjectNode.class));
    }

    @Test
    void testHasKey() {
        assertThat(firstLineCsvNode.hasKey("0"), equalTo(true));
    }

    @Test
    void testHasKey2() {
        assertThat(firstLineCsvNode.hasKey("1"), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        assertThat(firstLineCsvNode.hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testNotHasKeyString() {
        assertThat(firstLineCsvNode.hasKey("2"), equalTo(false));
    }

    @Test
    void testGet() {
        final StringHolderNode result = (StringHolderNode) firstLineCsvNode.get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetWithParentheses() {
        final CsvReader csvReaderParentheses = CsvReader.builder().build("\"foo1\",\"bar1\"\r\n\"foo2\",\"bar2\"");
        final DocumentNode firstLineNodeParentheses = new CsvObjectNode(csvReaderParentheses.iterator().next());
        final CsvObjectNode firstLineCsvNodeParentheses = (CsvObjectNode) firstLineNodeParentheses;
        final StringHolderNode result = (StringHolderNode) firstLineCsvNodeParentheses.get("0");

        assertThat(result.getValue(), equalTo("foo1"));
    }

    @Test
    void testGetKeyValueMap() {
        final CsvObjectNode objectNode = (CsvObjectNode) firstLineNode;
        final StringHolderNode result = (StringHolderNode) objectNode.getKeyValueMap().get("0");
        assertThat(result.getValue(), equalTo("foo1"));
    }
}
