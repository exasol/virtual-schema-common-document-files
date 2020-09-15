package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonObjectNodeTest {

    public static final JsonObject JSON_OBJECT = Json.createObjectBuilder().add("key", "value").build();

    @Test
    void testCreation() {
        final DocumentNode<JsonNodeVisitor> jsonNode = JsonNodeFactory.getInstance()
                .getJsonNode(Json.createObjectBuilder().build());
        assertThat(jsonNode, instanceOf(JsonObjectNode.class));
    }

    @Test
    void testHasKey() {
        final JsonObjectNode objectNode = (JsonObjectNode) JsonNodeFactory.getInstance().getJsonNode(JSON_OBJECT);
        assertThat(objectNode.hasKey("key"), equalTo(true));
    }

    @Test
    void testNotHasKey() {
        final JsonObjectNode objectNode = (JsonObjectNode) JsonNodeFactory.getInstance().getJsonNode(JSON_OBJECT);
        assertThat(objectNode.hasKey("unknownKey"), equalTo(false));
    }

    @Test
    void testGet() {
        final JsonObjectNode objectNode = (JsonObjectNode) JsonNodeFactory.getInstance().getJsonNode(JSON_OBJECT);
        final JsonStringNode result = (JsonStringNode) objectNode.get("key");
        assertThat(result.getStringValue(), equalTo("value"));
    }

    @Test
    void testGetKeyValueMap() {
        final JsonObjectNode objectNode = (JsonObjectNode) JsonNodeFactory.getInstance().getJsonNode(JSON_OBJECT);
        final JsonStringNode result = (JsonStringNode) objectNode.getKeyValueMap().get("key");
        assertThat(result.getStringValue(), equalTo("value"));
    }
}