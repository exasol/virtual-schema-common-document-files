package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.DataLoaderImpl;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for JSON-Lines {@link DataLoader}s.
 */
public class JsonLinesFilesDataLoaderFactory extends AbstractFilesDataLoaderFactory {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".jsonl");
    }

    @Override
    protected DataLoader buildSingleDataLoader(final FileLoaderFactory fileLoaderFactory,
            final SegmentDescription segmentDescription, final StringFilter sourceFilter) {
        return new DataLoaderImpl(new JsonLinesDocumentFetcher(sourceFilter, segmentDescription, fileLoaderFactory));
    }
}
