package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.ColumnNameConverter;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetSchemaFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for Parquet file {@link FileTypeSpecificDocumentFetcher}s.
 */
public class ParquetFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {
    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".parquet");
    }

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(
            final RemoteTableQuery remoteTableQuery) {
        return new ParquetDocumentFetcher();
    }

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher() {
        // FIXME: get configured column name converter
        return FileTypeSpecificSchemaFetcher
                .singleFile(new ParquetSchemaFetcher(ColumnNameConverter.upperSnakeCaseConverter()));
    }
}
