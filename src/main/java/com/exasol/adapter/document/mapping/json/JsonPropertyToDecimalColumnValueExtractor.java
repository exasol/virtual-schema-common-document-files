package com.exasol.adapter.document.mapping.json;

import java.math.BigDecimal;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.documentnode.json.JsonNumberNode;
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
    protected BigDecimal mapValueToDecimal(final DocumentNode<JsonNodeVisitor> documentValue) {
        if (documentValue instanceof JsonNumberNode) {
            final JsonNumberNode jsonNumber = (JsonNumberNode) documentValue;
            return jsonNumber.getValue();
        } else {
            return null;
        }
    }
}
