package com.exasol.adapter.document.documentnode.avro;

import java.util.Map;
import java.util.stream.Collectors;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

/**
 * {@link DocumentNode} wrapping a java map.
 */
public class JavaObjectNode implements DocumentObject {
    /** @serial */
    private final Map<String, Object> values;

    /**
     * Create a new instance of {@link JavaObjectNode}.
     * 
     * @param values java map to wrap
     */
    public JavaObjectNode(final Map values) {
        this.values = values;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        return this.values.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                entry -> JavaObjectDocumentNodeFactory.getNodeFor(entry.getValue())));
    }

    @Override
    public DocumentNode get(final String key) {
        return JavaObjectDocumentNodeFactory.getNodeFor(this.values.get(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return this.values.containsKey(key);
    }
}
