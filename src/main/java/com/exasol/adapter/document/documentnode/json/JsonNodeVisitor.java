package com.exasol.adapter.document.documentnode.json;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * Visitor interface for for JSON {@link DocumentNode}s.
 */
public interface JsonNodeVisitor {
    public void visit(JsonObjectNode jsonObjectNode);

    public void visit(JsonArrayNode jsonArrayNode);

    public void visit(JsonStringNode stringNode);

    public void visit(JsonNumberNode numberNode);

    public void visit(JsonNullNode nullNode);

    public void visit(JsonBooleanNode booleanNode);
}
