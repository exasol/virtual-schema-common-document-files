package com.exasol.adapter.document.documentnode.json;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.*;
import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

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
    public DocumentNode getJsonNode(final JsonValue jsonValue) {
        switch (jsonValue.getValueType()) {
        case OBJECT:
            return new JsonObjectNode(jsonValue.asJsonObject());
        case ARRAY:
            return new JsonArrayNode(jsonValue.asJsonArray());
        case STRING:
            final JsonString jsonString = (JsonString) jsonValue;
            return new StringHolderNode(jsonString.getString());
        case NULL:
            return new NullHolderNode();
        case TRUE:
            return new BooleanHolderNode(true);
        case FALSE:
            return new BooleanHolderNode(false);
        case NUMBER:
            final JsonNumber jsonNumber = (JsonNumber) jsonValue;
            return new BigDecimalHolderNode(jsonNumber.bigDecimalValue());
        default:
            throw new UnsupportedOperationException(ExaError.messageBuilder("F-VSDF-4")
                    .message("Unsupported json type: {{TYPE}}. Please open an issue.")
                    .parameter("TYPE", jsonValue.getValueType()).toString());
        }
    }
}
