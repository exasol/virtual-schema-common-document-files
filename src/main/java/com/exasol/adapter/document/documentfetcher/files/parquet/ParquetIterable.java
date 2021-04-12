package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.util.Iterator;

import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.hadoop.ParquetReader;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * Iterable for parquet files.
 */
class ParquetIterable implements Iterable<DocumentNode> {
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
    public Iterator<DocumentNode> iterator() {
        return new ParquetIterator(this.reader);
    }
}
