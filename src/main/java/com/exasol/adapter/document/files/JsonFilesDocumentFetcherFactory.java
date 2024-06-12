package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}.
 */
public class JsonFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(
            final RemoteTableQuery remoteTableQuery) {
        return new JsonDocumentFetcher();
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher() {
        return FileTypeSpecificSchemaFetcher.empty();
    }
}
