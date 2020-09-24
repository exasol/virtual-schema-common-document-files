package com.exasol.adapter.document.files;

import java.util.List;
import java.util.ServiceLoader;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.DataLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * This class resolves the matching {@link FilesDataLoaderFactory} depending on the file extension of the request.
 */
public class FilesDataLoaderResolver implements DataLoaderFactory {
    private final FileLoaderFactory fileLoaderFactory;

    /**
     * Get a new instance of {@link FilesDataLoaderResolver}.
     * 
     * @param fileLoaderFactory dependency in injection of {@link FileLoaderFactory}
     */
    public FilesDataLoaderResolver(final FileLoaderFactory fileLoaderFactory) {
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers) {
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        final ServiceLoader<FilesDataLoaderFactory> loader = ServiceLoader.load(FilesDataLoaderFactory.class);
        final FilesDataLoaderFactory filesDataLoaderFactory = loader.stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(sourceString::endsWith))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("Could not find a file type implementation for "
                        + sourceString + ". Please check the file extension."))//
                .get();
        return filesDataLoaderFactory.buildDataLoaderForQuery(remoteTableQuery, maxNumberOfParallelFetchers,
                this.fileLoaderFactory);
    }
}
