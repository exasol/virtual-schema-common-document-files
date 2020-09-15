package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.JsonValue;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonNullNodeTest {
    @Test
    void testCreation() {
        final DocumentNode<JsonNodeVisitor> jsonNode = JsonNodeFactory.getInstance().getJsonNode(JsonValue.NULL);
        assertThat(jsonNode, instanceOf(JsonNullNode.class));
    }
}