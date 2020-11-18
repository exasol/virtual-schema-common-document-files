package com.exasol.adapter.document.mapping.json;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToDecimalColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToDecimalColumnValueExtractor;

/**
 * {@link PropertyToDecimalColumnValueExtractor} for JSON {@link DocumentNode}s.
 */
public class JsonPropertyToDecimalColumnValueExtractor extends PropertyToDecimalColumnValueExtractor<JsonNodeVisitor> {

    /**
     * Create an instance of {@link JsonPropertyToDecimalColumnValueExtractor}.
     *
     * @param column {@link PropertyToDecimalColumnMapping} defining the mapping
     */
    public JsonPropertyToDecimalColumnValueExtractor(final PropertyToDecimalColumnMapping column) {
        super(column);
    }

    @Override
    protected ConversionResult mapValueToDecimal(final DocumentNode<JsonNodeVisitor> documentValue) {
        final ConversionVisitor conversionVisitor = new ConversionVisitor();
        documentValue.accept(conversionVisitor);
        return conversionVisitor.getResult();
    }

    private static class ConversionVisitor implements JsonNodeVisitor {
        private ConversionResult result;

        @Override
        public void visit(final JsonObjectNode jsonObjectNode) {
            this.result = new NotNumericResult("<object>");
        }

        @Override
        public void visit(final JsonArrayNode jsonArrayNode) {
            this.result = new NotNumericResult("<array>");
        }

        @Override
        public void visit(final JsonStringNode stringNode) {
            this.result = new NotNumericResult(stringNode.getStringValue());
        }

        @Override
        public void visit(final JsonNumberNode numberNode) {
            this.result = new ConvertedResult(numberNode.getValue());
        }

        @Override
        public void visit(final JsonNullNode nullNode) {
            this.result = new NotNumericResult("<null>");
        }

        @Override
        public void visit(final JsonBooleanNode booleanNode) {
            this.result = new NotNumericResult("<" + (booleanNode.getBooleanValue() ? "true" : "false") + ">");
        }

        /**
         * Get the result of the conversion.
         * 
         * @return result of the conversion
         */
        public ConversionResult getResult() {
            return this.result;
        }
    }
}
