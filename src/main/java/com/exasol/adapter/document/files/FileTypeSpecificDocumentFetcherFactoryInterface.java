package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Interface for factories for {@link FileTypeSpecificDocumentFetcher}.
 */
public interface FileTypeSpecificDocumentFetcherFactoryInterface {
    /**
     * Build a {@link FileTypeSpecificDocumentFetcher}.
     *
     * @param remoteTableQuery the document query
     * @return built {@link FileTypeSpecificDocumentFetcher}
     */
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(RemoteTableQuery remoteTableQuery);

    /**
     * Build a {@link FileTypeSpecificSchemaFetcher}.
     *
     * @return built {@link FileTypeSpecificSchemaFetcher}
     */
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher();

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
}
