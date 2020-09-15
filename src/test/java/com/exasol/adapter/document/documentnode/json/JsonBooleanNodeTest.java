package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.JsonValue;

import org.junit.jupiter.api.Test;

class JsonBooleanNodeTest {
    @Test
    void testCreateTrue() {
        final JsonBooleanNode jsonBoolean = (JsonBooleanNode) JsonNodeFactory.getInstance().getJsonNode(JsonValue.TRUE);
        assertThat(jsonBoolean.getBooleanValue(), equalTo(true));
    }

    @Test
    void testCreateFalse() {
        final JsonBooleanNode jsonBoolean = (JsonBooleanNode) JsonNodeFactory.getInstance()
                .getJsonNode(JsonValue.FALSE);
        assertThat(jsonBoolean.getBooleanValue(), equalTo(false));
    }
}