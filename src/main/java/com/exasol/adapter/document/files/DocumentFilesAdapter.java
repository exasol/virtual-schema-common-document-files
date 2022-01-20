package com.exasol.adapter.document.files;

import java.util.Collections;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.document.DocumentAdapterDialect;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.mapping.TableKeyFetcher;

/**
 * This class is the entry point for the files Virtual Schema.
 */
public class DocumentFilesAdapter implements DocumentAdapterDialect {
    private final String adapterName;
    private final FileLoaderFactory fileLoaderFactory;

    /**
     * Create a new instance of {@link DocumentFilesAdapter}.
     * 
     * @param adapterName       adapter name
     * @param fileLoaderFactory file storage specific file loader factory
     */
    public DocumentFilesAdapter(final String adapterName, final FileLoaderFactory fileLoaderFactory) {
        this.adapterName = adapterName;
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public TableKeyFetcher getTableKeyFetcher(final ConnectionPropertiesReader connectionInformation) {
        return (tableName, mappedColumns) -> Collections.emptyList();
    }

    @Override
    public final QueryPlanner getQueryPlanner(final ConnectionPropertiesReader connectionInformation,
            final AdapterProperties adapterProperties) {
        return new FilesQueryPlanner(this.fileLoaderFactory, connectionInformation);
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
        return this.fileLoaderFactory.getUserGuideUrl();
    }
}
