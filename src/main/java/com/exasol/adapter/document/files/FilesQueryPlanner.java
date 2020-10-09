package com.exasol.adapter.document.files;

import java.util.ServiceLoader;

import com.exasol.adapter.document.QueryPlan;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * This class plans the query on document files. For that, it resolves the matching {@link FilesDataLoaderFactory}
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
        final ServiceLoader<FilesDataLoaderFactory> loader = ServiceLoader.load(FilesDataLoaderFactory.class);
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString)
                .splitSelection(remoteTableQuery.getSelection());
        return getDataLoaderForFileExtension(maxNumberOfParallelFetchers, loader, splitSelection);
    }

    private QueryPlan getDataLoaderForFileExtension(final int maxNumberOfParallelFetchers,
            final ServiceLoader<FilesDataLoaderFactory> loader, final FilesSelectionExtractor.Result splitSelection) {
        final String filteredSourceString = splitSelection.getSourceString();
        final FilesDataLoaderFactory filesDataLoaderFactory = loader.stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(filteredSourceString::endsWith))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("Could not find a file type implementation for "
                        + filteredSourceString + ". Please check the file extension."))//
                .get();
        return new QueryPlan(filesDataLoaderFactory.buildDataLoaderForQuery(filteredSourceString,
                maxNumberOfParallelFetchers, this.fileLoaderFactory), splitSelection.getPostSelection());
    }
}
