package com.exasol.adapter.document.documentnode.parquet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentStringValue;
import com.exasol.parquetio.data.Row;

class RowRecordNodeTest {

    @Test
    void testGetValue() {
        final Row row = mock(Row.class);
        when(row.getValue("test")).thenReturn("value");
        final DocumentStringValue result = (DocumentStringValue) new RowRecordNode(row).get("test");
        assertThat(result.getValue(), equalTo("value"));
    }

    @Test
    void testGetKeyValueMap() {
        final Row row = mock(Row.class);
        when(row.getFieldNames()).thenReturn(List.of("key"));
        when(row.getValue("key")).thenReturn("value");
        final DocumentStringValue result = (DocumentStringValue) new RowRecordNode(row).getKeyValueMap().get("key");
        assertThat(result.getValue(), equalTo("value"));
    }

    @Test
    void testHasKey() {
        final Row row = mock(Row.class);
        when(row.hasFieldName("test")).thenReturn(true);
        assertThat(new RowRecordNode(row).hasKey("test"), equalTo(true));
    }
}