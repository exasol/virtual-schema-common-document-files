package com.exasol.adapter.document.documentnode.json;

import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * Factory for JSON {@link DocumentNode}s.
 */
public class JsonNodeFactory {
    private static final JsonNodeFactory INSTANCE = new JsonNodeFactory();

    private JsonNodeFactory() {
        // empty on purpose
    }

    /**
     * Get a singleton instance of {@link JsonNodeFactory}.
     * 
     * @return singleton instance of {@link JsonNodeFactory}
     */
    public static JsonNodeFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Build a JSON {@link DocumentNode} for a given JSON structure.
     * 
     * @param jsonValue JSON value
     * @return built JSON {@link DocumentNode}
     */
    public DocumentNode<JsonNodeVisitor> getJsonNode(final JsonValue jsonValue) {
        switch (jsonValue.getValueType()) {
        case OBJECT:
            return new JsonObjectNode(jsonValue.asJsonObject());
        case ARRAY:
            return new JsonArrayNode(jsonValue.asJsonArray());
        case STRING:
            return new JsonStringNode((JsonString) jsonValue);
        case NULL:
            return new JsonNullNode();
        case TRUE:
            return new JsonBooleanNode(true);
        case FALSE:
            return new JsonBooleanNode(false);
        case NUMBER:
            return new JsonNumberNode((JsonNumber) jsonValue);
        default:
            throw new UnsupportedOperationException("Unsupported json type: " + jsonValue.getValueType());
        }
    }
}
