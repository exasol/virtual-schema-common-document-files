package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.util.Iterator;

import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.hadoop.ParquetReader;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.avro.AvroNodeVisitor;

/**
 * Iterable for parquet files.
 */
class ParquetIterable implements Iterable<DocumentNode<AvroNodeVisitor>> {
    private final ParquetReader<GenericRecord> reader;

    /**
     * Create a new instance of {@link ParquetIterable}.
     * 
     * @param reader parquet reader
     */
    ParquetIterable(final ParquetReader<GenericRecord> reader) {
        this.reader = reader;
    }

    @Override
    public Iterator<DocumentNode<AvroNodeVisitor>> iterator() {
        return new ParquetIterator(this.reader);
    }
}
