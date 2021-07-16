package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.parquet.hadoop.ParquetReader;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.parquet.RowRecordNode;
import com.exasol.errorreporting.ExaError;
import com.exasol.parquetio.data.Row;

/**
 * Iterator for parquet files.
 */
class ParquetIterator implements Iterator<DocumentNode> {
    private final ParquetReader<Row> reader;
    private Row next;

    /**
     * Create a new instance of {@link ParquetIterator}.
     * 
     * @param reader parquet reader
     */
    ParquetIterator(final ParquetReader<Row> reader) {
        this.reader = reader;
        readNext();
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public DocumentNode next() {
        final Row current = this.next;
        if (current == null) {
            throw new NoSuchElementException(ExaError.messageBuilder("F-VSDF-8").message("No more elements available.")
                    .ticketMitigation().toString());
        }
        readNext();
        return new RowRecordNode(current);
    }

    private void readNext() {
        try {
            this.next = this.reader.read();
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-9").message("An error occurred during parquet parsing.").toString(),
                    exception);
        }
    }
}
