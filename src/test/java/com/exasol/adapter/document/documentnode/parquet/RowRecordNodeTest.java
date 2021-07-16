package com.exasol.adapter.document.documentnode.parquet;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.apache.parquet.schema.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentDecimalValue;
import com.exasol.parquetio.data.GenericRow;

class RowRecordNodeTest {
    public static final String COLUMN_NAME = "test";
    public static final int COLUMN_VALUE = 123;
    private GenericRow row;

    @BeforeEach
    void beforeEach() {
        final Type column = Types.primitive(INT32, REQUIRED).named(COLUMN_NAME);
        final MessageType schema = new MessageType("message", column);
        this.row = new GenericRow(schema, List.of(COLUMN_VALUE));
    }

    @Test
    void testGetValue() {
        final DocumentDecimalValue result = (DocumentDecimalValue) new RowRecordNode(this.row).get(COLUMN_NAME);
        assertThat(result.getValue().intValue(), equalTo(COLUMN_VALUE));
    }

    @Test
    void testGetKeyValueMap() {
        final DocumentDecimalValue result = (DocumentDecimalValue) new RowRecordNode(this.row).getKeyValueMap()
                .get(COLUMN_NAME);
        assertThat(result.getValue().intValue(), equalTo(COLUMN_VALUE));
    }

    @Test
    void testHasKey() {
        assertThat(new RowRecordNode(this.row).hasKey(COLUMN_NAME), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        assertThat(new RowRecordNode(this.row).hasKey("other"), equalTo(false));
    }
}