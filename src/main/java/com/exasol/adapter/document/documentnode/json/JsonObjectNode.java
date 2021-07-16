package com.exasol.adapter.document.documentnode.json;

import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

/**
 * This class represents JSON objects.
 */
public class JsonObjectNode implements DocumentObject {
    private static final long serialVersionUID = 4620082479753326426L;
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
    public Map<String, DocumentNode> getKeyValueMap() {
        return this.jsonObject.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> JsonNodeFactory.getInstance().getJsonNode(entry.getValue())));
    }

    @Override
    public DocumentNode get(final String key) {
        return JsonNodeFactory.getInstance().getJsonNode(this.jsonObject.get(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return this.jsonObject.containsKey(key);
    }
}
