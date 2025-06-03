package com.exasol.adapter.document.files;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.FilesDocumentFetcher;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDocumentFetcherFactory}
 * depending on the file extension of the request.
 */
public class FilesQueryPlanner implements QueryPlanner {
    private static final Logger LOGGER = Logger.getLogger(FilesQueryPlanner.class.getName());
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

    /**
     * Creates a {@link QueryPlan} based on the given {@link RemoteTableQuery} and the maximum number
     * of parallel document fetchers to use.
     *
     * <p>This method extracts the source string and splits the selection into source and post filters.
     * If a contradiction is detected in the source filter, an {@link EmptyQueryPlan} is returned.
     * If no document fetchers can be created, it also returns an {@link EmptyQueryPlan}.
     * Otherwise, a {@link FetchQueryPlan} is returned with the appropriate fetchers and post selection.
     *
     * @param remoteTableQuery the query that specifies the remote table and selection criteria
     * @param maxNumberOfParallelFetchers the maximum number of document fetchers to use concurrently
     * @return a {@link QueryPlan} representing how the query should be executed
     */
    @Override
    public QueryPlan planQuery(final RemoteTableQuery remoteTableQuery, final int maxNumberOfParallelFetchers) {
        // access the table mapping here
        final SourceString sourceString = getSourceString(remoteTableQuery);
        final FilesSelectionExtractor.Result splitSelection = getSplitSelection(sourceString, remoteTableQuery);
        if (splitSelection.getSourceFilter().hasContradiction()) {
            LOGGER.fine(() -> getSourceFilterContradictionLogMessage(sourceString, remoteTableQuery));
            return new EmptyQueryPlan();
        }
        final List<DocumentFetcher> documentFetchers = getDocumentFetchers(remoteTableQuery,
                maxNumberOfParallelFetchers, sourceString, splitSelection);
        if (documentFetchers.isEmpty()) {
            LOGGER.fine(() -> getEmptyDocumentFetchersLogMessage(sourceString, remoteTableQuery));
            return new EmptyQueryPlan();
        } else {
            return new FetchQueryPlan(documentFetchers, splitSelection.getPostSelection());
        }
    }

    /**
     * Extracts the {@link SourceString} from the remote table's name in the given {@link RemoteTableQuery}.
     *
     * @param remoteTableQuery the query containing the remote table information
     * @return a {@link SourceString} derived from the table's remote name
     */
    SourceString getSourceString(final RemoteTableQuery remoteTableQuery) {
        return new SourceString(remoteTableQuery.getFromTable().getRemoteName());
    }

    /**
     * Splits the selection in the given {@link RemoteTableQuery} into source and post filters
     * using the file pattern from the provided {@link SourceString}.
     *
     * @param sourceString the source string containing the file pattern
     * @param remoteTableQuery the query specifying the selection to split
     * @return a {@link FilesSelectionExtractor.Result} containing source and post selection filters
     */
    FilesSelectionExtractor.Result getSplitSelection(final SourceString sourceString, final RemoteTableQuery remoteTableQuery) {
        return new FilesSelectionExtractor(sourceString.getFilePattern())
                .splitSelection(remoteTableQuery.getSelection());
    }

    /**
     * Generates a log message indicating a contradiction in the source filter for the given query.
     *
     * @param sourceString the source string containing the file pattern
     * @param remoteTableQuery the query whose selection caused the contradiction
     * @return a formatted log message describing the contradiction and the fallback behavior
     */
    String getSourceFilterContradictionLogMessage(final SourceString sourceString, final RemoteTableQuery remoteTableQuery) {
        return String.format(
                "Contradiction detected in source filter for file pattern '%s' with selection '%s'. Returning EmptyQueryPlan.",
                sourceString.getFilePattern(),
                remoteTableQuery.getSelection()
        );
    }

    /**
     * Generates a log message indicating that no document fetchers were created for the given query.
     *
     * @param sourceString the source string containing the file pattern
     * @param remoteTableQuery the query that did not result in any document fetchers
     * @return a formatted log message describing the lack of fetchers and the fallback behavior
     */
    String getEmptyDocumentFetchersLogMessage(final SourceString sourceString, final RemoteTableQuery remoteTableQuery) {
        return String.format(
                "No document fetchers created for file pattern '%s' with selection '%s'. Returning EmptyQueryPlan.",
                sourceString.getFilePattern(),
                remoteTableQuery.getSelection()
        );
    }

    List<DocumentFetcher> getDocumentFetchers(final RemoteTableQuery remoteTableQuery,
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
