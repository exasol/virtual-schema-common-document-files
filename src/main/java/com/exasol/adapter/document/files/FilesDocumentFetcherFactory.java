package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * This interface defines factories for {@link DocumentFetcher}s for the files virtual schema. Classes implementing this
 * interface are loaded via a service loader. By that you can add support for new file types as a plugin.
 */
public interface FilesDocumentFetcherFactory {

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
     * Builds {@link DocumentFetcher}s for a given query.
     *
     * @param sourceFilter                filter for the source file names
     * @param maxNumberOfParallelFetchers the maximum amount of {@link DocumentFetcher}s that can be used in parallel
     * @param fileLoaderFactory           dependency injection of {@link FileLoaderFactory}
     * @return built {@link DocumentFetcher}
     */
    public List<DocumentFetcher> buildDocumentFetcherForQuery(final StringFilter sourceFilter,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory);
}
