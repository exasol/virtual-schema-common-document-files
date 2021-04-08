package com.exasol.adapter.document.documentnode.avro;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

/**
 * This class wraps an avro record.
 */
public class AvroRecordNode implements DocumentObject {
    private static final long serialVersionUID = 4901894006649229751L;
    /** @serial */
    private final GenericRecord record;

    /**
     * Create a new instance of {@link AvroRecordNode}.
     * 
     * @param record avro record to wrap
     */
    public AvroRecordNode(final GenericRecord record) {
        this.record = record;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        return this.record.getSchema().getFields().stream().collect(Collectors.toMap(Schema.Field::name,
                field -> JavaObjectDocumentNodeFactory.getNodeFor(this.record.get(field.name()))));
    }

    @Override
    public DocumentNode get(final String key) {
        return JavaObjectDocumentNodeFactory.getNodeFor(this.record.get(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return this.record.getSchema().getFields().stream().anyMatch(field -> field.name().equals(key));
    }
}
