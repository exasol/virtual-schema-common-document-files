package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.schema.MessageType;

import com.exasol.adapter.document.documentfetcher.files.ColumnNameConverter;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;
import com.exasol.errorreporting.ExaError;

/**
 * {@link SingleFileSchemaFetcher} for Parquet files.
 */
public class ParquetSchemaFetcher implements SingleFileSchemaFetcher {
    private final ColumnNameConverter columnNameConverter;

    public ParquetSchemaFetcher(final ColumnNameConverter columnNameConverter) {
        this.columnNameConverter = columnNameConverter;
    }

    @Override
    public InferredMappingDefinition fetchSchema(final RemoteFile remoteFile) {
        final MessageType schema = getType(remoteFile);
        final MappingDefinition mapping = new ParquetColumnToMappingDefinitionConverter(columnNameConverter)
                .convert(schema);
        return InferredMappingDefinition.builder(mapping).build();
    }

    private MessageType getType(final RemoteFile remoteFile) {
        final InputFile inputFile = SeekableInputStreamAdapter
                .convert(remoteFile.getContent().getRandomAccessInputStream());
        try (final var reader = ParquetFileReader.open(inputFile)) {
            return reader.getFileMetaData().getSchema();
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("E-VSDF-58")
                            .message("Failed to read parquet file {{file}}.", remoteFile.getResourceName()).toString(),
                    exception);
        }
    }
}
