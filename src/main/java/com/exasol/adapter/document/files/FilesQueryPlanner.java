package com.exasol.adapter.document.files;

import java.util.ServiceLoader;

import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.errorreporting.ExaError;

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
        final SourceString sourceString = new SourceString(remoteTableQuery.getFromTable().getRemoteName());
        final FilesSelectionExtractor.Result splitSelection = new FilesSelectionExtractor(sourceString.getFilePattern())
                .splitSelection(remoteTableQuery.getSelection());
        if (splitSelection.getSourceFilter().hasContradiction()) {
            return new EmptyQueryPlan();
        }
        final FilesDocumentFetcherFactory filesDocumentFetcherFactory = getFilesDataLoaderFactory(
                "." + sourceString.getFileType());
        return new FetchQueryPlan(
                filesDocumentFetcherFactory.buildDocumentFetcherForQuery(splitSelection.getSourceFilter(),
                        maxNumberOfParallelFetchers, this.fileLoaderFactory),
                splitSelection.getPostSelection());
    }

    private FilesDocumentFetcherFactory getFilesDataLoaderFactory(final String fileEnding) {
        final ServiceLoader<FilesDocumentFetcherFactory> loader = ServiceLoader.load(FilesDocumentFetcherFactory.class);
        return loader.stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(fileEnding::equalsIgnoreCase))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException(ExaError.messageBuilder("E-VSDF-13")
                        .message("Could not find a file type implementation for {{file ending}}.", fileEnding)
                        .mitigation("Please check the file extension.").toString()))//
                .get();
    }
}
