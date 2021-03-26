package com.exasol.adapter.document.documentnode.avro;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * Visitor for avro {@link DocumentNode}s.
 */
public interface AvroNodeVisitor {

    /**
     * Visit an {@link AvroRecordNode}.
     * 
     * @param recordNode {@link AvroRecordNode} to visit
     */
    public void visit(final AvroRecordNode recordNode);

    /**
     * Visit an {@link AvroValueNode}.
     *
     * @param valueNode {@link AvroValueNode} to visit
     */
    public void visit(final AvroValueNode valueNode);
}
