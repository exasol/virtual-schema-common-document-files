package com.exasol.adapter.document.files;

import java.util.Optional;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.mapping.auto.*;

class FilesSchemaFetcher implements SchemaFetcher {

    private final ConnectionPropertiesReader connectionInformation;
    private final FileTypeSpecificDocumentFetcherFactory documentFetcherFactory;
    private final FileFinderFactory fileFinderFactory;

    FilesSchemaFetcher(final FileFinderFactory fileFinderFactory,
            final FileTypeSpecificDocumentFetcherFactory documentFetcherFactory,
            final ConnectionPropertiesReader connectionInformation) {
        this.fileFinderFactory = fileFinderFactory;
        this.documentFetcherFactory = documentFetcherFactory;
        this.connectionInformation = connectionInformation;
    }

    @Override
    public Optional<InferredMappingDefinition> fetchSchema(final String source,
            final ColumnNameConverter columnNameConverter) {
        final SourceString sourceString = new SourceString(source);
        final FileTypeSpecificSchemaFetcher mappingFetcher = this.documentFetcherFactory
                .buildFileTypeSpecificSchemaFetcher("." + sourceString.getFileType());
        final RemoteFileFinder fileFinder = this.fileFinderFactory
                .getFinder(WildcardExpression.fromGlob(sourceString.getFilePattern()), this.connectionInformation);
        return mappingFetcher.fetchSchema(fileFinder, columnNameConverter);
    }
}
