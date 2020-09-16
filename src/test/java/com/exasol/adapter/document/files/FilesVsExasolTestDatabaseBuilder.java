package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.AbstractDataLoaderUdf.*;
import static com.exasol.adapter.document.UdfRequestDispatcher.UDF_PREFIX;
import static com.exasol.adapter.document.files.DocumentFilesAdapter.ADAPTER_NAME;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.exasol.adapter.document.UdfRequestDispatcher;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript;
import com.exasol.dbbuilder.dialects.exasol.ConnectionDefinition;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.github.dockerjava.api.model.ContainerNetwork;

public class FilesVsExasolTestDatabaseBuilder {
    public static final String BUCKETS_BFSDEFAULT_DEFAULT = "/buckets/bfsdefault/default/";
    public static final String DEBUGGER_PORT = "8000";
    private static final String VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION = "document-virtual-schema-dist-1.0.0-files-0.1.0.jar";
    public static final String VIRTUAL_SCHEMA_JAR_IN_BUCKET_FS = BUCKETS_BFSDEFAULT_DEFAULT
            + VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION;
    private static final Path PATH_TO_VIRTUAL_SCHEMAS_JAR = Path.of("target", VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
    private static final String FILES_ADAPTER = "FILES_ADAPTER";
    private static ExasolObjectFactory exasolObjectFactory;
    private final ExasolContainer<? extends ExasolContainer<?>> testContainer;
    private final AdapterScript adapterScript;
    private final ConnectionDefinition connection;
    private String mappingInBucketfs;

    public FilesVsExasolTestDatabaseBuilder(final ExasolContainer<? extends ExasolContainer<?>> testContainer)
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException {
        this.testContainer = testContainer;
        exasolObjectFactory = new ExasolObjectFactory(testContainer.createConnection());
        this.adapterScript = createAdapterScript();
        createUdf();
        this.connection = createConnectionDefinition();
    }

    private String getTestHostIp() {
        final Map<String, ContainerNetwork> networks = this.testContainer.getContainerInfo().getNetworkSettings()
                .getNetworks();
        if (networks.size() == 0) {
            return null;
        }
        return networks.values().iterator().next().getGateway();
    }

    public void createVirtualSchema(final String name, final Path mapping)
            throws InterruptedException, BucketAccessException, TimeoutException {
        this.mappingInBucketfs = "mapping.json";
        this.testContainer.getDefaultBucket().uploadFile(mapping, this.mappingInBucketfs);
        exasolObjectFactory//
                .createVirtualSchemaBuilder(name)//
                .connectionDefinition(this.connection)//
                .adapterScript(this.adapterScript)//
                .dialectName(ADAPTER_NAME)//
                .properties(Map.of("MAPPING", "/bfsdefault/default/" + this.mappingInBucketfs))//
                .build();
    }

    private ConnectionDefinition createConnectionDefinition() {
        return exasolObjectFactory.createConnectionDefinition("CONNECTION", "bucketfs:/bfsdefault/default/", "", "");
    }

    private AdapterScript createAdapterScript() throws InterruptedException, BucketAccessException, TimeoutException {
        this.testContainer.getDefaultBucket().uploadFile(PATH_TO_VIRTUAL_SCHEMAS_JAR,
                VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION);
        final ExasolSchema adapterSchema = exasolObjectFactory.createSchema("ADAPTER");
        return adapterSchema.createAdapterScriptBuilder().name(FILES_ADAPTER)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", VIRTUAL_SCHEMA_JAR_IN_BUCKET_FS)
                .language(AdapterScript.Language.JAVA).debuggerConnection(getTestHostIp() + ":" + DEBUGGER_PORT)
                .build();
    }

    // TODO refactor to use test-db-builder
    private void createUdf() throws SQLException {
        final StringBuilder statementBuilder = new StringBuilder(
                "CREATE OR REPLACE JAVA SET SCRIPT ADAPTER." + UDF_PREFIX + ADAPTER_NAME + "("
                        + PARAMETER_DOCUMENT_FETCHER + " VARCHAR(2000000), " + PARAMETER_REMOTE_TABLE_QUERY
                        + " VARCHAR(2000000), " + PARAMETER_CONNECTION_NAME + " VARCHAR(500)) EMITS(...) AS\n");
        // statementBuilder.append(getDebuggerOptions(true));
        statementBuilder.append("    %scriptclass " + UdfRequestDispatcher.class.getName() + ";\n");
        statementBuilder.append("    %jar /buckets/bfsdefault/default/" + VIRTUAL_SCHEMAS_JAR_NAME_AND_VERSION + ";\n");
        statementBuilder.append("/");
        final String sql = statementBuilder.toString();
        this.testContainer.createConnectionForUser(this.testContainer.getUsername(), this.testContainer.getPassword())
                .createStatement().execute(sql);
    }
}
