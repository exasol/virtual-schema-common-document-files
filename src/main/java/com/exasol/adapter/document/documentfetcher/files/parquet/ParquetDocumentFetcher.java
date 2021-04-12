package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.errorreporting.ExaError;

/**
 * {@link DocumentFetcher} for parquet files.
 */
public class ParquetDocumentFetcher extends AbstractFilesDocumentFetcher {
    private static final long serialVersionUID = -2501601443778189953L;

    /**
     * Create a new instance of {@link ParquetDocumentFetcher}.
     *
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    public ParquetDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        super(filePattern, segmentDescription, fileLoaderFactory);
    }

    @Override
    protected Stream<DocumentNode> readDocuments(final LoadedFile loadedFile) {
        final InputFile hadoopInputFile = SeekableInputStreamAdapter.convert(loadedFile.getRandomAccessInputStream());
        try {
            final ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(hadoopInputFile)
                    .build();
            return StreamSupport.stream(new ParquetIterable(reader).spliterator(), false)
                    .onClose(() -> tryToCloseReader(reader));
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-7").message("Failed to read parquet file.").toString(), exception);
        }
    }

    private void tryToCloseReader(final ParquetReader<GenericRecord> reader) {
        try {
            reader.close();
        } catch (final IOException exception) {
            // doesn't matter
        }
    }
}
