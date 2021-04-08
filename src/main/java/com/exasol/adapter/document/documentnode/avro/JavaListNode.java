package com.exasol.adapter.document.documentnode.avro;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.adapter.document.documentnode.DocumentArray;
import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class wraps list of java objects as {@link DocumentNode}.
 */
public class JavaListNode implements DocumentArray {
    /** @serial */
    private final List<Object> values;

    public JavaListNode(final List<Object> values) {
        this.values = values;
    }

    @Override
    public List<? extends DocumentNode> getValuesList() {
        return this.values.stream().map(JavaObjectDocumentNodeFactory::getNodeFor).collect(Collectors.toList());
    }

    @Override
    public DocumentNode getValue(final int index) {
        return JavaObjectDocumentNodeFactory.getNodeFor(this.values.get(index));
    }

    @Override
    public int size() {
        return this.values.size();
    }
}
