package com.exasol.adapter.document.files;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.ColumnNameConverter;
import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.csv.CsvSchemaFetcher;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for {@link FileTypeSpecificDocumentFetcher}.
 */
public class CsvFilesDocumentFetcherFactory implements FileTypeSpecificDocumentFetcherFactoryInterface {

    @Override
    public FileTypeSpecificDocumentFetcher buildFileTypeSpecificDocumentFetcher(
            final RemoteTableQuery remoteTableQuery) {
        return new CsvDocumentFetcher(remoteTableQuery.getFromTable().getColumns());
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".csv");
    }

    @Override
    public FileTypeSpecificSchemaFetcher buildFileTypeSpecificMappingFetcher(
            final ColumnNameConverter columnNameConverter) {
        return FileTypeSpecificSchemaFetcher.singleFile(new CsvSchemaFetcher(columnNameConverter));
    }
}
