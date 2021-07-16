package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON-Lines {@link DocumentFetcher}s.
 */
public class ParquetFilesDocumentFetcherFactory extends AbstractFilesDocumentFetcherFactory {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".parquet");
    }

    @Override
    protected DocumentFetcher buildSingleDocumentFetcher(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        return new ParquetDocumentFetcher(sourceFilter, segmentDescription, fileLoaderFactory);
    }
}
