package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;

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
            final SegmentDescription segmentDescription, final String sourceString) {
        return new JsonFilesDataLoader(
                new JsonLinesDocumentFetcher(sourceString, segmentDescription, fileLoaderFactory));
    }
}
