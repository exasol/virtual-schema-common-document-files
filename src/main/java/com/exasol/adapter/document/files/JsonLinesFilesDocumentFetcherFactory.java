package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}s.
 */
public class JsonLinesFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".jsonl");
    }

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher() {
        return new JsonLinesDocumentFetcher();
    }
}
