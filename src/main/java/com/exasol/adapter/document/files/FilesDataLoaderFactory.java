package com.exasol.adapter.document.files;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.DataLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Files {@link DataLoaderFactory} implementation.
 * <p>
 * This {@link DataLoaderFactory} builds the {@link DataLoader} corresponding to the file ending of the source name. If
 * the file name contains a pattern, like {@code *.json}, the loading is distributed over multiple document fetchers.
 * </p>
 */
public class FilesDataLoaderFactory implements DataLoaderFactory {
    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers) {
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        if (sourceString.endsWith(JsonDocumentFetcher.FILE_EXTENSION)) {
            return buildJsonDataLoader(maxNumberOfParallelFetchers, sourceString);
        } else if (sourceString.endsWith(JsonLinesDocumentFetcher.FILE_EXTENSION)) {
            validateNoGlob(sourceString, "JSON-Lines");
            return buildJsonLinesDataLoader(sourceString);
        } else {
            throw new IllegalArgumentException(
                    "Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]");
        }
    }

    private List<DataLoader> buildJsonDataLoader(final int maxNumberOfParallelFetchers, final String sourceString) {
        final List<DataLoader> dataLoaders = new ArrayList<>(maxNumberOfParallelFetchers);
        for (int segmentCounter = 0; segmentCounter < maxNumberOfParallelFetchers; segmentCounter++) {
            final JsonDocumentFetcher documentFetcher = new JsonDocumentFetcher(sourceString,
                    new SegmentDescription(maxNumberOfParallelFetchers, segmentCounter));
            dataLoaders.add(new JsonDataLoader(documentFetcher));
        }
        return dataLoaders;
    }

    private List<DataLoader> buildJsonLinesDataLoader(final String sourceString) {
        return List.of(new JsonDataLoader(new JsonLinesDocumentFetcher(sourceString)));
    }

    private void validateNoGlob(final String sourceString, final String typeName) {
        if (sourceString.contains("*")) {
            throw new IllegalArgumentException("Invalid source '" + sourceString + "'. For the file type " + typeName
                    + " you must specify exactly one file.");
        }
    }
}
