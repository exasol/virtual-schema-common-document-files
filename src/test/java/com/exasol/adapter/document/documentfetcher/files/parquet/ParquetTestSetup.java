package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
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

    public ParquetTestSetup(final Path tempDir, final Type... columnTypes) throws IOException {
        this.parquetFile = Files.createTempFile(tempDir, "testData", ".parquet");
        Files.delete(this.parquetFile);
        this.schema = new MessageType("test", columnTypes);
        this.parquetWriter = new ParquetTestWriterBuilder(this.parquetFile, this.schema).build();
    }

    public Path getParquetFile() {
        return this.parquetFile;
    }

    public ParquetTestSetup writeRow(final RowBuilder rowBuilder) throws IOException {
        final SimpleGroup record = new SimpleGroup(this.schema);
        rowBuilder.populateRecord(record);
        this.parquetWriter.write(record);
        return this;
    }

    public void closeWriter() throws IOException {
        this.parquetWriter.close();
    }

    @FunctionalInterface
    public interface RowBuilder {
        void populateRecord(final Group recordToFill);
    }
}
