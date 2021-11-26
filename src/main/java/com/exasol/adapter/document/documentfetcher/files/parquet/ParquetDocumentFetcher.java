package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.util.Collections;
import java.util.List;

import org.apache.parquet.io.InputFile;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescriptionMatcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.parquet.RowRecordNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.*;
import com.exasol.parquetio.data.ChunkInterval;
import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetChunkReader;
import com.exasol.parquetio.splitter.ParquetFileSplitter;

/**
 * {@link FileTypeSpecificDocumentFetcher} for parquet files.
 */
public class ParquetDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = -1626621753088123668L;
    private static final long SPLIT_CHUNK_SIZE = 16L * 1024;

    @Override
    public CloseableIterator<DocumentNode> readDocuments(final FileSegment segment) {
        final InputFile hadoopInputFile = SeekableInputStreamAdapter
                .convert(segment.getFile().getRandomAccessInputStream());
        final ParquetFileSplitter splitter = new ParquetFileSplitter(hadoopInputFile, SPLIT_CHUNK_SIZE);
        final List<ChunkInterval> splits = splitter.getSplits();
        final List<ChunkInterval> thisSegment = new FileSegmentDescriptionMatcher(segment.getSegmentDescription())
                .filter(splits);
        if (thisSegment.isEmpty()) {
            return new CloseableIteratorWrapper<>(Collections.emptyIterator());
        } else {
            final RowParquetChunkReader reader = new RowParquetChunkReader(hadoopInputFile, thisSegment);
            final RowParquetChunkReader.RowIterator rowIterator = reader.iterator();
            final CloseableIterator<Row> closableRowIterator = new CloseableIteratorWrapper<>(rowIterator,
                    rowIterator::close);
            return new TransformingIterator<>(closableRowIterator, RowRecordNode::new);
        }
    }

    @Override
    public boolean supportsFileSplitting() {
        return true;
    }
}
