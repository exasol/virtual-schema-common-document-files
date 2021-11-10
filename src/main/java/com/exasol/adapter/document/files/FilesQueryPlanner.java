package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

import lombok.RequiredArgsConstructor;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDocumentFetcherFactory}
 * depending on the file extension of the request.
 */
@RequiredArgsConstructor
public class FilesQueryPlanner implements QueryPlanner {
    private final FileLoaderFactory fileLoaderFactory;
    private final ExaConnectionInformation connectionInformation;

    @Override
    public QueryPlan planQuery(final RemoteTableQuery remoteTableQuery, final int maxNumberOfParallelFetchers) {
        final SourceString sourceString = new SourceString(remoteTableQuery.getFromTable().getRemoteName());
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString.getFilePattern())
                .splitSelection(remoteTableQuery.getSelection());
        if (splitSelection.getSourceFilter().hasContradiction()) {
            return new EmptyQueryPlan();
        }
        final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher = new FileTypeSpecificDocumentFetcherFactory()
                .buildFileTypeSpecificDocumentFetcher("." + sourceString.getFileType());
        final List<DocumentFetcher> documentFetchers = new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(
                splitSelection.getSourceFilter(), maxNumberOfParallelFetchers, this.fileLoaderFactory,
                this.connectionInformation, fileTypeSpecificDocumentFetcher);
        return new FetchQueryPlan(documentFetchers, splitSelection.getPostSelection());
    }
}
