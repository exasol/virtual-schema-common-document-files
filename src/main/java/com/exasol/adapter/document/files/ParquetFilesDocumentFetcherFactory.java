package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetSchemaFetcher;

/**
 * Factory for Parquet file {@link FileTypeSpecificDocumentFetcher}s.
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

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher() {
        return FileTypeSpecificSchemaFetcher.singleFile(new ParquetSchemaFetcher());
    }
}
