package com.exasol.adapter.document.documentnode.json;

import javax.json.JsonString;

import com.exasol.adapter.document.documentnode.DocumentValue;

/**
 * This class represents JSON string values.
 */
public class JsonStringNode implements DocumentValue<JsonNodeVisitor> {

    private final String stringValue;

    /**
     * Create a new {@link JsonStringNode}.
     * 
     * @param jsonString JSON string to wrap.
     */
    public JsonStringNode(final JsonString jsonString) {
        this.stringValue = jsonString.getString();
    }

    /**
     * Get the string value.
     * 
     * @return string value
     */
    public String getStringValue() {
        return this.stringValue;
    }

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }
}
