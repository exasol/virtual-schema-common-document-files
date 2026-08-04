package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.Type;

public class ParquetTestSetup {
    private final Path parquetFile;
    private final MessageType schema;
    private final ParquetWriter<Group> parquetWriter;

    public ParquetTestSetup(final Path tempDir, final Type... columnTypes) {
        try {
            this.parquetFile = Files.createTempFile(tempDir, "testData", ".parquet");
            Files.delete(this.parquetFile);
            this.schema = new MessageType("test", columnTypes);
            this.parquetWriter = new ParquetTestWriterBuilder(this.parquetFile, this.schema).build();
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to create parquet test setup in directory: " + tempDir, e);
        }
    }

    public Path getParquetFile() {
        this.closeWriter();
        return this.parquetFile;
    }

    public ParquetTestSetup writeRow(final RowBuilder rowBuilder) {
        final SimpleGroup recordGroup = new SimpleGroup(this.schema);
        rowBuilder.populateRecord(recordGroup);
        try {
            this.parquetWriter.write(recordGroup);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to write record to parquet file: " + this.parquetFile, e);
        }
        return this;
    }

    public void closeWriter() {
        try {
            this.parquetWriter.close();
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to close parquet writer for file: " + this.parquetFile, e);
        }
    }

    @FunctionalInterface
    public interface RowBuilder {
        void populateRecord(final Group recordToFill);
    }
}
