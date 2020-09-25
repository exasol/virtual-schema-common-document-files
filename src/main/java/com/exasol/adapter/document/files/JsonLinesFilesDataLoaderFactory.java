package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for JSON-Lines {@link DataLoader}s.
 */
public class JsonLinesFilesDataLoaderFactory implements FilesDataLoaderFactory {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".jsonl");
    }

    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory) {
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        if (sourceString.contains("*") || sourceString.contains("?")) {
            throw new IllegalArgumentException("Invalid source '" + sourceString
                    + "'. For the JSON-Lines you must specify exactly one file. * and ? wildcards are not allowed.");
        }
        return List.of(new JsonDataLoader(new JsonLinesDocumentFetcher(sourceString, fileLoaderFactory)));
    }
}
