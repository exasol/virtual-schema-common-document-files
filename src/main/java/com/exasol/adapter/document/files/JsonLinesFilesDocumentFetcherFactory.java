package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.ColumnNameConverter;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}s.
 */
public class JsonLinesFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {
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
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher(
            final ColumnNameConverter columnNameConverter) {
        return FileTypeSpecificSchemaFetcher.empty();
    }
}
