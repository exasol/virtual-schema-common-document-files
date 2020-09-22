package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.files.DocumentFilesAdapter.ADAPTER_NAME;

import java.util.Set;

import com.exasol.adapter.AdapterFactory;
import com.exasol.adapter.VirtualSchemaAdapter;
import com.exasol.adapter.document.files.DocumentFilesAdapter;
import com.exasol.logging.VersionCollector;

/**
 * Factory for {@link DocumentFilesAdapter}.
 */
public class DocumentFilesAdapterFactory implements AdapterFactory {
    @Override
    public Set<String> getSupportedAdapterNames() {
        return Set.of(ADAPTER_NAME);
    }

    @Override
    public VirtualSchemaAdapter createAdapter() {
        return new BucketfsDocumentFilesAdapter();
    }

    @Override
    public String getAdapterVersion() {
        final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/virtual-schema-common-document-files/pom.properties");
        return versionCollector.getVersionNumber();
    }

    @Override
    public String getAdapterName() {
        return ADAPTER_NAME;
    }
}
