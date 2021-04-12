package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.DataLoaderImpl;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON {@link DataLoader}.
 */
public class JsonFilesDataLoaderFactory extends AbstractFilesDataLoaderFactory {

    @Override
    protected DataLoader buildSingleDataLoader(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        final JsonDocumentFetcher documentFetcher = new JsonDocumentFetcher(sourceFilter, segmentDescription,
                fileLoaderFactory);
        return new DataLoaderImpl(documentFetcher);
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }
}
