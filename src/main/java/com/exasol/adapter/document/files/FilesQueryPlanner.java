package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDocumentFetcherFactory}
 * depending on the file extension of the request.
 */
public class FilesQueryPlanner implements QueryPlanner {
    private final FileFinderFactory fileFinderFactory;
    private final ConnectionPropertiesReader connectionInformation;

    /**
     * Create a new {@link FilesQueryPlanner}.
     *
     * @param fileFinderFactory     file finder factory
     * @param connectionInformation connection information reader
     */
    public FilesQueryPlanner(final FileFinderFactory fileFinderFactory,
            final ConnectionPropertiesReader connectionInformation) {
        this.fileFinderFactory = fileFinderFactory;
        this.connectionInformation = connectionInformation;
    }

    @Override
    public QueryPlan planQuery(final RemoteTableQuery remoteTableQuery, final int maxNumberOfParallelFetchers) {
        // access the table mapping here
        final SourceString sourceString = new SourceString(remoteTableQuery.getFromTable().getRemoteName());
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString.getFilePattern())
                .splitSelection(remoteTableQuery.getSelection());
        if (splitSelection.getSourceFilter().hasContradiction()) {
            return new EmptyQueryPlan();
        }
        final List<DocumentFetcher> documentFetchers = getDocumentFetchers(remoteTableQuery,
                maxNumberOfParallelFetchers, sourceString, splitSelection);
        if (documentFetchers.isEmpty()) {
            return new EmptyQueryPlan();
        } else {
            return new FetchQueryPlan(documentFetchers, splitSelection.getPostSelection());
        }
    }

    private List<DocumentFetcher> getDocumentFetchers(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers, final SourceString sourceString,
            final FilesSelectionExtractor.Result splitSelection) {
        final String additionalConfiguration = remoteTableQuery.getFromTable().getAdditionalConfiguration();
        return new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(splitSelection.getSourceFilter(),
                maxNumberOfParallelFetchers, this.fileFinderFactory, this.connectionInformation,
                createDocumentFetcher(remoteTableQuery, sourceString), additionalConfiguration);
    }

    private FileTypeSpecificDocumentFetcher createDocumentFetcher(final RemoteTableQuery remoteTableQuery,
            final SourceString sourceString) {
        final FileTypeSpecificDocumentFetcherFactory factory = new FileTypeSpecificDocumentFetcherFactory();
        // .csv,.json,.jsonlines,.parquet, etc.
        final String fileEnding = "." + sourceString.getFileType();
        return factory.buildFileTypeSpecificDocumentFetcher(fileEnding, remoteTableQuery);
    }
}
