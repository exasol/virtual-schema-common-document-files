package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON-Lines {@link DocumentFetcher}s.
 */
public class JsonLinesFilesDocumentFetcherFactory extends AbstractFilesDocumentFetcherFactory {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".jsonl");
    }

    @Override
    protected DocumentFetcher buildSingleDocumentFetcher(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        return new JsonLinesDocumentFetcher(sourceFilter, segmentDescription, fileLoaderFactory);
    }
}
