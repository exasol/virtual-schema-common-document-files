package com.exasol.adapter.document.files;

import java.util.ServiceLoader;

import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDocumentFetcherFactory}
 * depending on the file extension of the request.
 */
public class FilesQueryPlanner implements QueryPlanner {
    private final FileLoaderFactory fileLoaderFactory;

    /**
     * Get a new instance of {@link FilesQueryPlanner}.
     * 
     * @param fileLoaderFactory dependency in injection of {@link FileLoaderFactory}
     */
    public FilesQueryPlanner(final FileLoaderFactory fileLoaderFactory) {
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public QueryPlan planQuery(final RemoteTableQuery remoteTableQuery, final int maxNumberOfParallelFetchers) {
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString)
                .splitSelection(remoteTableQuery.getSelection());
        if (splitSelection.getSourceFilter().hasContradiction()) {
            return new EmptyQueryPlan();
        }
        final FilesDocumentFetcherFactory filesDocumentFetcherFactory = getFilesDataLoaderFactory(sourceString);
        return new FetchQueryPlan(
                filesDocumentFetcherFactory.buildDocumentFetcherForQuery(splitSelection.getSourceFilter(),
                        maxNumberOfParallelFetchers, this.fileLoaderFactory),
                splitSelection.getPostSelection());
    }

    private FilesDocumentFetcherFactory getFilesDataLoaderFactory(final String sourceFilterGlob) {
        final ServiceLoader<FilesDocumentFetcherFactory> loader = ServiceLoader.load(FilesDocumentFetcherFactory.class);
        return loader.stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(sourceFilterGlob::endsWith))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("Could not find a file type implementation for "
                        + sourceFilterGlob + ". Please check the file extension."))//
                .get();
    }
}
