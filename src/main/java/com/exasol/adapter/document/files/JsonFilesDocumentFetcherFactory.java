package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;

/**
 * Factory for JSON {@link DocumentFetcher}.
 */
public class JsonFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher() {
        return new JsonDocumentFetcher();
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }
}
