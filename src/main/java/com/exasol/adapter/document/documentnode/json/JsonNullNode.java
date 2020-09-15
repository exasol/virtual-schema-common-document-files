package com.exasol.adapter.document.documentnode.json;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class represents JSON NULL values.
 */
public class JsonNullNode implements DocumentNode<JsonNodeVisitor> {

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }
}
