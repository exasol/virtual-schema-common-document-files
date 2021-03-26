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
public class AvroRecordNode implements DocumentObject<AvroNodeVisitor> {
    private static final long serialVersionUID = 3349393522994678022L;
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
    public Map<String, DocumentNode<AvroNodeVisitor>> getKeyValueMap() {
        return this.record.getSchema().getFields().stream().collect(Collectors.toMap(Schema.Field::name,
                field -> wrapChildNode(this.record.get(field.name()), field.schema())));
    }

    @Override
    public DocumentNode<AvroNodeVisitor> get(final String key) {
        return wrapChildNode(this.record.get(key), this.record.getSchema().getField(key).schema());
    }

    @Override
    public boolean hasKey(final String key) {
        return this.record.getSchema().getFields().stream().anyMatch(field -> field.name().equals(key));
    }

    private DocumentNode<AvroNodeVisitor> wrapChildNode(final Object value, final Schema schema) {
        return new AvroValueNode(schema, value);
    }

    @Override
    public void accept(final AvroNodeVisitor visitor) {
        visitor.visit(this);
    }
}
