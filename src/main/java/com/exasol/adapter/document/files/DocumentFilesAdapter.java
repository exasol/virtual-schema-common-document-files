package com.exasol.adapter.document.files;

import java.util.Collections;

import com.exasol.ExaConnectionInformation;
import com.exasol.ExaMetadata;
import com.exasol.adapter.AdapterException;
import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.capabilities.MainCapability;
import com.exasol.adapter.document.DataLoaderUdf;
import com.exasol.adapter.document.DocumentAdapter;
import com.exasol.adapter.document.documentfetcher.DocumentFetcherFactory;
import com.exasol.adapter.document.documentfetcher.files.FilesDocumentFetcherFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.json.JsonDataLoaderUdf;
import com.exasol.adapter.document.mapping.TableKeyFetcher;
import com.exasol.adapter.request.GetCapabilitiesRequest;
import com.exasol.adapter.response.GetCapabilitiesResponse;

/**
 * This class is the entry point for the files Virtual Schema.
 */
public class DocumentFilesAdapter extends DocumentAdapter<JsonNodeVisitor> {
    public static final String ADAPTER_NAME = "DOCUMENT_FILES";

    @Override
    protected TableKeyFetcher getTableKeyFetcher(final ExaConnectionInformation connectionInformation)
            throws AdapterException {
        return (tableName, mappedColumns) -> Collections.emptyList();
    }

    @Override
    protected DocumentFetcherFactory<JsonNodeVisitor> getDocumentFetcherFactory(
            final ExaConnectionInformation connectionInformation) throws AdapterException {
        return new FilesDocumentFetcherFactory();
    }

    @Override
    protected String getAdapterName() {
        return ADAPTER_NAME;
    }

    @Override
    public GetCapabilitiesResponse getCapabilities(final ExaMetadata exaMetadata,
            final GetCapabilitiesRequest getCapabilitiesRequest) throws AdapterException {
        return GetCapabilitiesResponse.builder()
                .capabilities(Capabilities.builder().addMain(MainCapability.SELECTLIST_PROJECTION).build()).build();
    }

    @Override
    public DataLoaderUdf getDataLoaderUDF() {
        return new JsonDataLoaderUdf();
    }
}
