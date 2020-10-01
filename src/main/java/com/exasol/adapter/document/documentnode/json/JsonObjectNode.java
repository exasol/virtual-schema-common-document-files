package com.exasol.adapter.document.documentnode.json;

import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

/**
 * This class represents JSON objects.
 */
public class JsonObjectNode implements DocumentObject<JsonNodeVisitor> {
    /** @serial */
    private final JsonObject jsonObject;

    /**
     * Create a new instance of {@link JsonObjectNode}.
     * 
     * @param jsonObject JSON object to wrap
     */
    public JsonObjectNode(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public Map<String, DocumentNode<JsonNodeVisitor>> getKeyValueMap() {
        return this.jsonObject.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> JsonNodeFactory.getInstance().getJsonNode(entry.getValue())));
    }

    @Override
    public DocumentNode<JsonNodeVisitor> get(final String key) {
        return JsonNodeFactory.getInstance().getJsonNode(this.jsonObject.get(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return this.jsonObject.containsKey(key);
    }

    @Override
    public void accept(final JsonNodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get the wrapped JSON object.
     * 
     * @return wrapped JSON object
     */
    public JsonObject getJsonObject() {
        return this.jsonObject;
    }
}
