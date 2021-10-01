package com.exasol.adapter.document.documentnode.json;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.adapter.document.documentnode.DocumentArray;
import com.exasol.adapter.document.documentnode.DocumentNode;

import jakarta.json.JsonArray;

/**
 * This class represents JSON arrays.
 */
public class JsonArrayNode implements DocumentArray {
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
    public List<DocumentNode> getValuesList() {
        return this.jsonArray.stream().map(each -> JsonNodeFactory.getInstance().getJsonNode(each))
                .collect(Collectors.toList());
    }

    @Override
    public DocumentNode getValue(final int index) {
        return JsonNodeFactory.getInstance().getJsonNode(this.jsonArray.get(index));
    }

    @Override
    public int size() {
        return this.jsonArray.size();
    }
}
