package com.exasol.adapter.document.documentnode.parquet;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.objectwrapper.ObjectWrapperDocumentNodeFactory;
import com.exasol.parquetio.data.Row;

/**
 * {@link DocumentNode} that wraps {@link Row} data generated from parquet-io-java.
 */
public class RowRecordNode implements DocumentObject {
    private final Row row;

    /**
     * Create a new instance of {@link RowRecordNode}.
     * 
     * @param row row to wrap
     */
    public RowRecordNode(final Row row) {
        this.row = row;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> result = new HashMap<>();
        for (final String fieldName : this.row.getFieldNames()) {
            final DocumentNode wrappedFieldValue = ObjectWrapperDocumentNodeFactory
                    .getNodeFor(this.row.getValue(fieldName));
            result.put(fieldName, wrappedFieldValue);
        }
        return result;
    }

    @Override
    public DocumentNode get(final String key) {
        return ObjectWrapperDocumentNodeFactory.getNodeFor(this.row.getValue(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return this.row.hasFieldName(key);
    }
}
