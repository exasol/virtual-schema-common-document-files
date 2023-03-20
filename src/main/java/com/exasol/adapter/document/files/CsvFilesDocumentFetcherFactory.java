package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}.
 */
public class CsvFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher() {
        return new CsvDocumentFetcher();
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".csv");
    }

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher() {
        return FileTypeSpecificSchemaFetcher.unsupported();
    }
}
