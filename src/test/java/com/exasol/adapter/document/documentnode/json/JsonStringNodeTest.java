package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.Json;

import org.junit.jupiter.api.Test;

class JsonStringNodeTest {

    @Test
    void testCreation() {
        final JsonStringNode jsonString = (JsonStringNode) JsonNodeFactory.getInstance()
                .getJsonNode(Json.createValue("test"));
        assertThat(jsonString.getStringValue(), equalTo("test"));
    }
}