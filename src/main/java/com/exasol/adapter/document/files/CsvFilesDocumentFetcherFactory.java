package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;

import java.util.List;

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
}
