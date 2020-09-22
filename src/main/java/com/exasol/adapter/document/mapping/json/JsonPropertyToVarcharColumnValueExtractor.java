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
    protected MappedStringResult mapStringValue(final DocumentNode<JsonNodeVisitor> property) {
        final ToStringVisitor toStringVisitor = new ToStringVisitor();
        property.accept(toStringVisitor);
        return toStringVisitor.getResult();
    }

    private static class ToStringVisitor implements JsonNodeVisitor {
        private MappedStringResult result;

        @Override
        public void visit(final JsonObjectNode jsonObjectNode) {
            this.result = null;
        }

        @Override
        public void visit(final JsonArrayNode jsonArrayNode) {
            this.result = null;
        }

        @Override
        public void visit(final JsonStringNode stringNode) {
            this.result = new MappedStringResult(stringNode.getStringValue(), false);
        }

        @Override
        public void visit(final JsonNumberNode numberNode) {
            this.result = new MappedStringResult(numberNode.getValue().toString(), true);
        }

        @Override
        public void visit(final JsonNullNode nullNode) {
            this.result = new MappedStringResult(null, false);
        }

        @Override
        public void visit(final JsonBooleanNode booleanNode) {
            this.result = new MappedStringResult(booleanNode.getBooleanValue() ? "true" : "false", true);
        }

        public MappedStringResult getResult() {
            return this.result;
        }
    }
}
