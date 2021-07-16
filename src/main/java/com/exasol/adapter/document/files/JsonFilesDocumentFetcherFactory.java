package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON {@link DocumentFetcher}.
 */
public class JsonFilesDocumentFetcherFactory extends AbstractFilesDocumentFetcherFactory {

    @Override
    protected DocumentFetcher buildSingleDocumentFetcher(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        return new JsonDocumentFetcher(sourceFilter, segmentDescription, fileLoaderFactory);
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }
}
