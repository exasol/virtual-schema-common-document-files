package com.exasol.adapter.document.documentnode.json;

import java.util.List;
import java.util.stream.Collectors;

import javax.json.JsonArray;

import com.exasol.adapter.document.documentnode.DocumentArray;
import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class represents JSON arrays.
 */
public class JsonArrayNode implements DocumentArray<JsonNodeVisitor> {
    /** @serial */
    private final JsonArray jsonArray;

    /**
     * Create a new instance of {@link JsonArrayNode}.
     * 
     * @param jsonArray JSON array to wrap
     */
    public JsonArrayNode(final JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public List<? extends DocumentNode<JsonNodeVisitor>> getValuesList() {
        return this.jsonArray.stream().map(each -> JsonNodeFactory.getInstance().getJsonNode(each))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentNode<JsonNodeVisitor> getValue(final int index) {
        return JsonNodeFactory.getInstance().getJsonNode(this.jsonArray.get(index));
    }

    @Override
    public int size() {
        return this.jsonArray.size();
    }

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get the wrapped JSON array.
     * 
     * @return wrapped JSON array
     */
    public JsonArray getJsonArray() {
        return this.jsonArray;
    }
}
