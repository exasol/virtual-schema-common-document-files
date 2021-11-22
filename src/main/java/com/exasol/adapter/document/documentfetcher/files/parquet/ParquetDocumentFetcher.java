package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.util.*;

import org.apache.parquet.io.InputFile;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescriptionMatcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.parquet.RowRecordNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.AfterAllCallbackIterator;
import com.exasol.adapter.document.iterators.TransformingIterator;
import com.exasol.parquetio.data.ChunkInterval;
import com.exasol.parquetio.reader.RowParquetChunkReader;
import com.exasol.parquetio.splitter.ParquetFileSplitter;

/**
 * {@link FileTypeSpecificDocumentFetcher} for parquet files.
 */
public class ParquetDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 1657802428914102318L;
    private static final long SPLIT_CHUNK_SIZE = 16L * 1024;

    @Override
    public Iterator<DocumentNode> readDocuments(final FileSegment segment) {
        final InputFile hadoopInputFile = SeekableInputStreamAdapter
                .convert(segment.getFile().getRandomAccessInputStream());
        final ParquetFileSplitter splitter = new ParquetFileSplitter(hadoopInputFile, SPLIT_CHUNK_SIZE);
        final List<ChunkInterval> splits = splitter.getSplits();
        final List<ChunkInterval> thisSegment = new FileSegmentDescriptionMatcher(segment.getSegmentDescription())
                .filter(splits);
        if (thisSegment.isEmpty()) {
            return Collections.emptyIterator();
        } else {
            final RowParquetChunkReader reader = new RowParquetChunkReader(hadoopInputFile, thisSegment);
            final RowParquetChunkReader.RowIterator rowIterator = reader.iterator();
            final Iterator<DocumentNode> rowRecordIterator = new TransformingIterator<>(rowIterator,
                    RowRecordNode::new);
            return new AfterAllCallbackIterator<>(rowRecordIterator, () -> tryToClose(rowIterator));
        }
    }

    void tryToClose(final AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (final Exception e) {
            // at least we tried
        }
    }

    @Override
    public boolean supportsFileSplitting() {
        return true;
    }
}
