package com.exasol.adapter.document.files;

import java.util.ServiceLoader;

import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.errorreporting.ExaError;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}.
 */
public class FileTypeSpecificDocumentFetcherFactory {

    /**
     * Build a {@link FileTypeSpecificDocumentFetcher} for a given file name extension.
     *
     * @param fileEnding       file ending including the leading {@code "."}
     * @param remoteTableQuery the document query
     * @return built {@link FileTypeSpecificDocumentFetcher}
     */
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(final String fileEnding,
            final RemoteTableQuery remoteTableQuery) {
        return findFactory(fileEnding).buildFileTypeSpecificDocumentFetcher(remoteTableQuery);
    }

    /**
     * Build a {@link FileTypeSpecificSchemaFetcher} for a given file name extension.
     *
     * @param fileEnding file ending including {@code .}
     * @return built {@link FileTypeSpecificSchemaFetcher}
     */
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificSchemaFetcher(final String fileEnding) {
        return findFactory(fileEnding).buildFileTypeSpecificMappingFetcher();
    }

    // you select the factory and then return the document fetcher
    private FileTypeSpecificDocumentFetcherFactoryInterface findFactory(final String fileEnding) {
        return ServiceLoader.load(FileTypeSpecificDocumentFetcherFactoryInterface.class).stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(fileEnding::equalsIgnoreCase))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException(ExaError.messageBuilder("E-VSDF-13")
                        .message("Could not find a file type implementation for {{file ending}}.", fileEnding)
                        .mitigation("Please check the file extension.").toString()))//
                .get();
    }
}
