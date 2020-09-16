package com.exasol.adapter.document.documentfetcher.files;

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
            return List.of(new JsonDocumentFetcher(sourceString));
        } else if (sourceString.endsWith(JsonLinesDocumentFetcher.FILE_EXTENSION)) {
            return List.of(new JsonLinesDocumentFetcher(sourceString));
        } else {
            throw new IllegalArgumentException(
                    "Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]");
        }
    }
}
