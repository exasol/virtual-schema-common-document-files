package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.util.Iterator;

import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;

import com.exasol.adapter.document.AfterAllCallbackIterator;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.errorreporting.ExaError;
import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;

/**
 * {@link DocumentFetcher} for parquet files.
 */
public class ParquetDocumentFetcher extends AbstractFilesDocumentFetcher {
    private static final long serialVersionUID = 1840717648925583476L;

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
    protected Iterator<DocumentNode> readDocuments(final LoadedFile loadedFile) {
        final InputFile hadoopInputFile = SeekableInputStreamAdapter.convert(loadedFile.getRandomAccessInputStream());
        try {
            final ParquetReader<Row> reader = RowParquetReader.builder(hadoopInputFile).build();
            return new AfterAllCallbackIterator<>(new ParquetIterator(reader), () -> tryToCloseReader(reader));
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-7").message("Failed to read parquet file.").toString(), exception);
        }
    }

    private void tryToCloseReader(final ParquetReader<Row> reader) {
        try {
            reader.close();
        } catch (final IOException exception) {
            // doesn't matter
        }
    }
}
