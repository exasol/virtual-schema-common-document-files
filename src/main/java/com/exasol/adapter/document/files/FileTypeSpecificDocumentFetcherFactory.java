package com.exasol.adapter.document.files;

import java.util.ServiceLoader;

import com.exasol.errorreporting.ExaError;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}.
 */
public class FileTypeSpecificDocumentFetcherFactory {

    /**
     * Build a {@link FileTypeSpecificDocumentFetcher} for given file ending.
     * 
     * @param fileEnding file ending including {@code .}
     * @return built {@link FileTypeSpecificDocumentFetcher}
     */
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(final String fileEnding) {
        return findFactory(fileEnding).buildFileTypeSpecificDocumentFetcher();
    }

    private FileTypeSpecificDocumentFetcherFactoryInterface findFactory(final String fileEnding) {
        final ServiceLoader<FileTypeSpecificDocumentFetcherFactoryInterface> loader = ServiceLoader
                .load(FileTypeSpecificDocumentFetcherFactoryInterface.class);
        return loader.stream()
                .filter(x -> x.get().getSupportedFileExtensions().stream().anyMatch(fileEnding::equalsIgnoreCase))//
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException(ExaError.messageBuilder("E-VSDF-13")
                        .message("Could not find a file type implementation for {{file ending}}.", fileEnding)
                        .mitigation("Please check the file extension.").toString()))//
                .get();
    }
}
