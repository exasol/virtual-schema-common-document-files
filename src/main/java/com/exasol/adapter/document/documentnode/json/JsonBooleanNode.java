package com.exasol.adapter.document.documentnode.json;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class represents JSON boolean values.
 */
public class JsonBooleanNode implements DocumentNode<JsonNodeVisitor> {
    /** @serial */
    private final boolean booleanValue;

    /**
     * Create an instance of {@link JsonBooleanNode}.
     * 
     * @param booleanValue boolean value to wrap
     */
    public JsonBooleanNode(final boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    /**
     * Get the boolean value.
     * 
     * @return boolean value
     */
    public boolean getBooleanValue() {
        return this.booleanValue;
    }

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }
}
