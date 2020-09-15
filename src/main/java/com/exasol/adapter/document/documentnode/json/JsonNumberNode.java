package com.exasol.adapter.document.documentnode.json;

import java.math.BigDecimal;

import javax.json.JsonNumber;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class represents JSON numbers.
 */
public class JsonNumberNode implements DocumentNode<JsonNodeVisitor> {
    private final BigDecimal numberValue;

    /**
     * Create a new instance of {@link JsonNumberNode}.
     * 
     * @param jsonNumber json number to wrap
     */
    public JsonNumberNode(final JsonNumber jsonNumber) {
        this.numberValue = jsonNumber.bigDecimalValue();
    }

    /**
     * Get the number value.
     * 
     * @return number value
     */
    public BigDecimal getValue() {
        return this.numberValue;
    }

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }
}
