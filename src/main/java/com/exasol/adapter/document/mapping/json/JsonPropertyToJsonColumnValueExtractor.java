package com.exasol.adapter.document.mapping.json;

import javax.json.JsonValue;
import javax.json.spi.JsonProvider;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToJsonColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToJsonColumnValueExtractor;

/**
 * {@link PropertyToJsonColumnValueExtractor} for JSON {@link DocumentNode}s.
 */
public class JsonPropertyToJsonColumnValueExtractor extends PropertyToJsonColumnValueExtractor<JsonNodeVisitor> {
    private static final JsonProvider JSON = JsonProvider.provider();

    /**
     * Create an instance of {@link JsonPropertyToJsonColumnValueExtractor}.
     *
     * @param column {@link PropertyToJsonColumnMapping}
     */
    public JsonPropertyToJsonColumnValueExtractor(final PropertyToJsonColumnMapping column) {
        super(column);
    }

    @Override
    protected String mapJsonValue(final DocumentNode<JsonNodeVisitor> property) {
        final ToJsonVisitor visitor = new ToJsonVisitor();
        property.accept(visitor);
        return visitor.getJsonValue().toString();
    }

    private static class ToJsonVisitor implements JsonNodeVisitor {
        JsonValue jsonValue;

        @Override
        public void visit(final JsonObjectNode jsonObjectNode) {
            this.jsonValue = jsonObjectNode.getJsonObject();
        }

        @Override
        public void visit(final JsonArrayNode jsonArrayNode) {
            this.jsonValue = jsonArrayNode.getJsonArray();
        }

        @Override
        public void visit(final JsonStringNode stringNode) {
            this.jsonValue = JSON.createValue(stringNode.getStringValue());
        }

        @Override
        public void visit(final JsonNumberNode numberNode) {
            this.jsonValue = JSON.createValue(numberNode.getValue());
        }

        @Override
        public void visit(final JsonNullNode nullNode) {
            this.jsonValue = JsonValue.NULL;
        }

        @Override
        public void visit(final JsonBooleanNode booleanNode) {
            this.jsonValue = booleanNode.getBooleanValue() ? JsonValue.TRUE : JsonValue.FALSE;
        }

        public JsonValue getJsonValue() {
            return this.jsonValue;
        }
    }
}
