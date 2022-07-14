package com.exasol.adapter.document.documentnode.csv;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.NamedCsvReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

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
        assertThat(firstLineCsvNode.hasKey("2"),equalTo(false));
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
    //Tests for CSV with headers
    public static final NamedCsvReader csvWithHeadersReader = NamedCsvReader.builder().build("header1,header2\r\nfoo1,bar1\r\nfoo2,bar2");
    public static final DocumentNode csvWithHeadersFirstLineNode = new CsvObjectNode(csvWithHeadersReader.iterator().next());
    public static final CsvObjectNode csvWithHeadersFirstLineCsvNode = (CsvObjectNode) csvWithHeadersFirstLineNode;
    @Test
    void testWithHeadersCreation() {
        assertThat(csvWithHeadersFirstLineNode, instanceOf(CsvObjectNode.class));
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
        assertThat(csvWithHeadersFirstLineCsvNode.hasKey("header3"),equalTo(false));
    }
    @Test
    void testWHGet() {

        final StringHolderNode result = (StringHolderNode) csvWithHeadersFirstLineCsvNode.get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }
    @Test
    void testWHGetWithParentheses() {
        final NamedCsvReader csvReaderParentheses = NamedCsvReader.builder().build("header1,header2\r\n\"foo1\",\"bar1\"\r\n\"foo2\",\"bar2\"");
        final DocumentNode firstLineNodeParentheses = new CsvObjectNode(csvReaderParentheses.iterator().next());
        final CsvObjectNode firstLineCsvNodeParentheses = (CsvObjectNode) firstLineNodeParentheses;
        final StringHolderNode result = (StringHolderNode) firstLineCsvNodeParentheses.get("header1");

        assertThat(result.getValue(), equalTo("foo1"));
    }
    @Test
    void testWHGetKeyValueMap() {
        final CsvObjectNode objectNode = (CsvObjectNode) csvWithHeadersFirstLineNode;
        final StringHolderNode result = (StringHolderNode) objectNode.getKeyValueMap().get("header1");
        assertThat(result.getValue(), equalTo("foo1"));
    }


}
