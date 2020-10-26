package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON {@link JsonFilesDataLoader}.
 */
public class JsonFilesDataLoaderFactory extends AbstractFilesDataLoaderFactory {

    @Override
    protected JsonFilesDataLoader buildSingleDataLoader(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        final JsonDocumentFetcher documentFetcher = new JsonDocumentFetcher(sourceFilter, segmentDescription,
                fileLoaderFactory);
        return new JsonFilesDataLoader(documentFetcher);
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }
}
