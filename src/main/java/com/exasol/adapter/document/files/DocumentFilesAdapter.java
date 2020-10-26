package com.exasol.adapter.document.files;

import java.util.Collections;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.capabilities.LiteralCapability;
import com.exasol.adapter.capabilities.MainCapability;
import com.exasol.adapter.capabilities.PredicateCapability;
import com.exasol.adapter.document.DocumentAdapter;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.mapping.TableKeyFetcher;

/**
 * This class is the entry point for the files Virtual Schema.
 */
public abstract class DocumentFilesAdapter extends DocumentAdapter {

    @Override
    protected TableKeyFetcher getTableKeyFetcher(final ExaConnectionInformation connectionInformation)
            throws AdapterException {
        return (tableName, mappedColumns) -> Collections.emptyList();
    }

    @Override
    protected final QueryPlanner getQueryPlanner(final ExaConnectionInformation connectionInformation)
            throws AdapterException {
        return new FilesQueryPlanner(getFileLoaderFactory());
    }

    /**
     * Get the file backend specific {@link FileLoaderFactory}.
     * 
     * @return file backend specific {@link FileLoaderFactory}
     */
    protected abstract FileLoaderFactory getFileLoaderFactory();

    @Override
    protected Capabilities getCapabilities() {
        return Capabilities.builder().addMain(MainCapability.SELECTLIST_PROJECTION, MainCapability.FILTER_EXPRESSIONS)
                .addPredicate(PredicateCapability.EQUAL, PredicateCapability.LIKE, PredicateCapability.AND,
                        PredicateCapability.OR, PredicateCapability.NOT)
                .addLiteral(LiteralCapability.STRING).build();
    }
}
