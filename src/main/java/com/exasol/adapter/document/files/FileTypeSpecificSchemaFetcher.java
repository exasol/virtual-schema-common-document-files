package com.exasol.adapter.document.files;

import java.util.Optional;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.mapping.auto.ColumnNameConverter;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;
import com.exasol.errorreporting.ExaError;

/**
 * Interface for file type specific mapping-fetcher implementations.
 */
@FunctionalInterface
public interface FileTypeSpecificSchemaFetcher {

    /**
     * Detect the schema of the given file or files.
     *
     * @param fileFinder          the file finder providing the files for which to detect the mapping.
     * @param columnNameConverter the column name converter to use
     * @return an empty {@link Optional} if this file type does not support schema detection or the
     *         {@link MappingDefinition}.
     */
    Optional<InferredMappingDefinition> fetchSchema(RemoteFileFinder fileFinder,
            ColumnNameConverter columnNameConverter);

    /**
     * Create a new {@link FileTypeSpecificSchemaFetcher} that always returns an empty {@link Optional}.
     *
     * Use this in {@link FileTypeSpecificDocumentFetcherFactoryInterface#buildFileTypeSpecificMappingFetcher()} for
     * file types that do not support detecting the mapping.
     *
     * @return a {@link FileTypeSpecificSchemaFetcher} that always returns an empty {@link Optional}
     */
    public static FileTypeSpecificSchemaFetcher empty() {
        return (final RemoteFileFinder fileFinder, final ColumnNameConverter columnNameConverter) -> Optional.empty();
    }

    /**
     * This interface fetches the schema of a single {@link RemoteFile}.
     */
    @FunctionalInterface
    public interface SingleFileSchemaFetcher {
        /**
         * Fetches the schema of the given {@link RemoteFile}.
         *
         * @param remoteFile          the file for which to fetch the mapping
         * @param columnNameConverter the column name converter to use
         * @return the fetched mapping
         */
        InferredMappingDefinition fetchSchema(RemoteFile remoteFile, ColumnNameConverter columnNameConverter);
    }

    /**
     * Create a {@link FileTypeSpecificSchemaFetcher} that fetches the schema based on the first file. Other files that
     * may use a different schema are ignored.
     *
     * @param delegate a {@link SingleFileSchemaFetcher} that is called for the first matched file
     * @return a {@link FileTypeSpecificSchemaFetcher} that detects the schema based on the first matching file
     * @throws IllegalStateException if the {@link RemoteFileFinder} does not return any file
     */
    public static FileTypeSpecificSchemaFetcher singleFile(final SingleFileSchemaFetcher delegate) {
        return (final RemoteFileFinder fileFinder, final ColumnNameConverter columnNameConverter) -> {
            try (CloseableIterator<RemoteFile> files = fileFinder.loadFiles()) {
                if (!files.hasNext()) {
                    throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-57")
                            .message("Error detecting mapping because no file matches the SOURCE expression.")
                            .mitigation("Specify a source that matches files.")
                            .mitigation("Or specify the 'mapping' element in the JSON EDML definition.").toString());
                }
                return Optional.of(delegate.fetchSchema(files.next(), columnNameConverter));
            }
        };
    }
}
