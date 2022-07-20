package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.queryplan.EmptyQueryPlan;
import com.exasol.adapter.document.queryplan.FetchQueryPlan;
import com.exasol.adapter.document.queryplan.QueryPlan;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

import lombok.RequiredArgsConstructor;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDocumentFetcherFactory}
 * depending on the file extension of the request.
 */
@RequiredArgsConstructor
public class FilesQueryPlanner implements QueryPlanner {
    private final FileFinderFactory fileFinderFactory;
    private final ConnectionPropertiesReader connectionInformation;

    @Override
    public QueryPlan planQuery(final RemoteTableQuery remoteTableQuery, final int maxNumberOfParallelFetchers) {
        // access the table mapping here
        final SourceString sourceString = new SourceString(remoteTableQuery.getFromTable().getRemoteName());
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString.getFilePattern())
                .splitSelection(remoteTableQuery.getSelection());
        if (splitSelection.getSourceFilter().hasContradiction()) {
            return new EmptyQueryPlan();
        }
        String additionalConfiguration = remoteTableQuery.getFromTable().getAdditionalConfiguration();
        // .csv,.json,.jsonlines,.parquet, etc.
        String fileEnding = "." + sourceString.getFileType();
        final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher = new FileTypeSpecificDocumentFetcherFactory()
                .buildFileTypeSpecificDocumentFetcher(fileEnding);
        final List<DocumentFetcher> documentFetchers = new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(
                splitSelection.getSourceFilter(), maxNumberOfParallelFetchers, this.fileFinderFactory,
                this.connectionInformation, fileTypeSpecificDocumentFetcher, additionalConfiguration);
        if (documentFetchers.isEmpty()) {
            return new EmptyQueryPlan();
        } else {
            return new FetchQueryPlan(documentFetchers, splitSelection.getPostSelection());
        }
    }
}
