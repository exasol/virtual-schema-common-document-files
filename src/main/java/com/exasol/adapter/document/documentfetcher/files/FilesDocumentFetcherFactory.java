package com.exasol.adapter.document.documentfetcher.files;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.DocumentFetcherFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Files {@link DocumentFetcherFactory} implementation.
 * <p>
 * This {@link DocumentFetcherFactory} builds the {@link DocumentFetcher} corresponding to the file ending of the source
 * name. If the file name contains a pattern, like {@code *.json}, the loading is distributed over multiple document
 * fetchers.
 * </p>
 */
public class FilesDocumentFetcherFactory implements DocumentFetcherFactory<JsonNodeVisitor> {
    @Override
    public List<DocumentFetcher<JsonNodeVisitor>> buildDocumentFetcherForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers) {
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        if (sourceString.endsWith(JsonDocumentFetcher.FILE_EXTENSION)) {
            final List<DocumentFetcher<JsonNodeVisitor>> documentFetchers = new ArrayList<>(
                    maxNumberOfParallelFetchers);
            for (int segmentCounter = 0; segmentCounter < maxNumberOfParallelFetchers; segmentCounter++) {
                documentFetchers.add(new JsonDocumentFetcher(sourceString,
                        new SegmentDescription(maxNumberOfParallelFetchers, segmentCounter)));
            }
            return documentFetchers;
        } else if (sourceString.endsWith(JsonLinesDocumentFetcher.FILE_EXTENSION)) {
            validateNoGlob(sourceString, "JSON-Lines");
            return List.of(new JsonLinesDocumentFetcher(sourceString));
        } else {
            throw new IllegalArgumentException(
                    "Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]");
        }
    }

    private void validateNoGlob(final String sourceString, final String typeName) {
        if (sourceString.contains("*")) {
            throw new IllegalArgumentException("Invalid source '" + sourceString + "'. For the file type " + typeName
                    + " you must specify exactly one file.");
        }
    }
}
