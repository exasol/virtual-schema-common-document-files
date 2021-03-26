package com.exasol.adapter.document.documentnode.avro;

import org.apache.avro.Schema;

import com.exasol.adapter.document.documentnode.DocumentValue;

/**
 * This class wraps an avro value.
 */
public class AvroValueNode implements DocumentValue<AvroNodeVisitor> {
    private static final long serialVersionUID = 6360154702720036415L;
    /** @serial */
    private final Schema schema;
    /** @serial */
    private final Object value;

    /**
     * Create a new instance of {@link AvroValueNode}.
     * 
     * @param schema node's schema
     * @param value  node's value
     */
    public AvroValueNode(final Schema schema, final Object value) {
        this.schema = schema;
        this.value = value;
    }

    /**
     * Get the value of the avro object.
     * 
     * @return value of the avro object.
     */
    public Object getValue() {
        return this.value;
    }

    @Override
    public void accept(final AvroNodeVisitor visitor) {
        visitor.visit(this);
    }
}
