package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;

/**
 * Factory for JSON-Lines {@link DocumentFetcher}s.
 */
public class ParquetFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".parquet");
    }

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher() {
        return new ParquetDocumentFetcher();
    }
}
