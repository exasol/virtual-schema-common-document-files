package com.exasol.adapter.document.documentnode.json;

import static com.exasol.adapter.document.documentnode.util.DocumentNodeMatchers.stringNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;

import jakarta.json.Json;
import jakarta.json.JsonObject;

class JsonObjectNodeTest {

    public static final JsonObject JSON_OBJECT = Json.createObjectBuilder().add("key", "value").build();

    @Test
    void testCreation() {
        final DocumentNode jsonNode = JsonNodeFactory.getInstance().getJsonNode(Json.createObjectBuilder().build());
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
        assertThat(objectNode.get("key"), stringNode("value"));
    }

    @Test
    void testGetKeyValueMap() {
        final JsonObjectNode objectNode = (JsonObjectNode) JsonNodeFactory.getInstance().getJsonNode(JSON_OBJECT);
        assertThat(objectNode.getKeyValueMap().get("key"), stringNode("value"));
    }
}
