package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;

/**
 * This interface defines factories for {@link DataLoader}s for the files virtual schema. Classes implementing this
 * interface are loaded via a service loader. By that you can add support for new file types as a plugin.
 */
public interface FilesDataLoaderFactory {

    /**
     * Get the file extensions for that this factory can build {@link DocumentFetcher}s.
     *
     * <p>
     * The extensions must include a {@code .}. For example {@code .json}.
     * </p>
     *
     * @return list of supported file extensions
     */
    public List<String> getSupportedFileExtensions();

    /**
     * Builds {@link DataLoader}s for a given query.
     *
     * @param sourceString                the source string to fetch the data for
     * @param maxNumberOfParallelFetchers the maximum amount of {@link DocumentFetcher}s that can be used in parallel
     * @param fileLoaderFactory           dependency injection of {@link FileLoaderFactory}
     * @return built {@link DocumentFetcher}
     */
    public List<DataLoader> buildDataLoaderForQuery(final String sourceString, final int maxNumberOfParallelFetchers,
            final FileLoaderFactory fileLoaderFactory);
}
