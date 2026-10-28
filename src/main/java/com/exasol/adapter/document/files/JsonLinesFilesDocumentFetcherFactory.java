package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}s.
 */
public class JsonLinesFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {
    /**
     * Creates a factory for JSON Lines document fetchers.
     */
    public JsonLinesFilesDocumentFetcherFactory() {
        // Default constructor.
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".jsonl");
    }

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(
            final RemoteTableQuery remoteTableQuery) {
        return new JsonLinesDocumentFetcher();
    }

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher() {
        return FileTypeSpecificSchemaFetcher.empty();
    }
}
