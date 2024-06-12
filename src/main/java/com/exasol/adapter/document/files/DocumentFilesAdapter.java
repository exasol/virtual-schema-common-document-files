package com.exasol.adapter.document.files;

import java.util.Collections;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.document.DocumentAdapterDialect;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.ColumnNameConverter;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.mapping.TableKeyFetcher;
import com.exasol.adapter.document.mapping.auto.SchemaFetcher;

/**
 * This class is the entry point for the files Virtual Schema.
 */
public class DocumentFilesAdapter implements DocumentAdapterDialect {
    private final String adapterName;
    private final FileFinderFactory fileFinderFactory;
    private final ColumnNameConverter columnNameConverter;

    /**
     * Create a new instance of {@link DocumentFilesAdapter}.
     *
     * @param adapterName         adapter name
     * @param fileFinderFactory   file storage specific file loader factory
     * @param columnNameConverter column name converter
     */
    public DocumentFilesAdapter(final String adapterName, final FileFinderFactory fileFinderFactory,
            final ColumnNameConverter columnNameConverter) {
        this.adapterName = adapterName;
        this.fileFinderFactory = fileFinderFactory;
        this.columnNameConverter = columnNameConverter;
    }

    @Override
    public TableKeyFetcher getTableKeyFetcher(final ConnectionPropertiesReader connectionInformation) {
        return (tableName, mappedColumns) -> Collections.emptyList();
    }

    @Override
    public SchemaFetcher getSchemaFetcher(final ConnectionPropertiesReader connectionInformation) {
        return new FilesSchemaFetcher(this.fileFinderFactory,
                new FileTypeSpecificDocumentFetcherFactory(this.columnNameConverter), connectionInformation);
    }

    @Override
    public final QueryPlanner getQueryPlanner(final ConnectionPropertiesReader connectionInformation,
            final AdapterProperties adapterProperties) {
        return new FilesQueryPlanner(this.columnNameConverter, this.fileFinderFactory, connectionInformation);
    }

    @Override
    public String getAdapterName() {
        return this.adapterName;
    }

    @Override
    public Capabilities getCapabilities() {
        return Capabilities.builder().addMain(MainCapability.SELECTLIST_PROJECTION, MainCapability.FILTER_EXPRESSIONS)
                .addPredicate(PredicateCapability.EQUAL, PredicateCapability.LIKE, PredicateCapability.AND,
                        PredicateCapability.OR, PredicateCapability.NOT)
                .addLiteral(LiteralCapability.STRING).build();
    }

    @Override
    public String getUserGuideUrl() {
        return this.fileFinderFactory.getUserGuideUrl();
    }
}
