package com.exasol.adapter.document.mapping.json;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnValueExtractor;

/**
 * This class is a {@link PropertyToVarcharColumnValueExtractor} for JSON {@link DocumentNode}s.
 */
public class JsonPropertyToVarcharColumnValueExtractor extends PropertyToVarcharColumnValueExtractor<JsonNodeVisitor> {

    /**
     * Create an instance of {@link JsonPropertyToVarcharColumnValueExtractor}.
     *
     * @param column {@link PropertyToVarcharColumnMapping}
     */
    public JsonPropertyToVarcharColumnValueExtractor(final PropertyToVarcharColumnMapping column) {
        super(column);
    }

    @Override
    protected String mapStringValue(final DocumentNode<JsonNodeVisitor> property) {
        final ToStringVisitor toStringVisitor = new ToStringVisitor();
        property.accept(toStringVisitor);
        return toStringVisitor.getResult();
    }

    private static class ToStringVisitor implements JsonNodeVisitor {
        private String result;

        @Override
        public void visit(final JsonObjectNode jsonObjectNode) {
            canNotConvert("Object");
        }

        @Override
        public void visit(final JsonArrayNode jsonArrayNode) {
            canNotConvert("Array");
        }

        @Override
        public void visit(final JsonStringNode stringNode) {
            this.result = stringNode.getStringValue();
        }

        @Override
        public void visit(final JsonNumberNode numberNode) {
            this.result = numberNode.getValue().toString();
        }

        @Override
        public void visit(final JsonNullNode nullNode) {
            this.result = null;
        }

        @Override
        public void visit(final JsonBooleanNode booleanNode) {
            this.result = booleanNode.getBooleanValue() ? "true" : "false";
        }

        private void canNotConvert(final String type) {
            throw new UnsupportedOperationException("The JSON type " + type + " can not be converted to string.");
        }

        public String getResult() {
            return this.result;
        }
    }
}
