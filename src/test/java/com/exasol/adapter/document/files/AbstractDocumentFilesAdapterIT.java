package com.exasol.adapter.document.files;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.udfdebugging.PushDownTesting.getPushDownSql;
import static com.exasol.udfdebugging.PushDownTesting.getSelectionThatIsSentToTheAdapter;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.function.Supplier;

import org.apache.parquet.schema.Type;
import org.apache.parquet.schema.Types;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetTestSetup;

@SuppressWarnings("java:S5786") // this class is public so that class from different packages can inherit
public abstract class AbstractDocumentFilesAdapterIT {
    private static final String TEST_SCHEMA = "TEST";
    private static final String IT_RESOURCES = "abstractIntegrationTests/";

    protected abstract Statement getStatement();

    protected abstract void uploadDataFile(final Supplier<InputStream> resource, String resourceName);

    private void createVirtualSchema(final String schemaName, final String resourceName) {
        createVirtualSchema(schemaName, () -> AbstractDocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(IT_RESOURCES + resourceName));
    }

    protected abstract void createVirtualSchema(String schemaName, Supplier<InputStream> mapping);

    @Test
    void testReadJson() throws SQLException {
        createJsonVirtualSchema();
        final ResultSet result = getStatement()
                .executeQuery("SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA + ".BOOKS ORDER BY ID ASC;");
        assertThat(result, table().row("book-1", "testData-1.json").row("book-2", "testData-2.json").matches());
    }

    @Test
    void testReadJsonLines() throws SQLException {
        createJsonLinesVirtualSchema();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testJsonDataTypesAsVarcharColumn() throws SQLException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToVarchar.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", equalTo(null))//
                .row("number", "1.23")//
                .row("string", "test")//
                .row("true", "true")//
                .matches());
    }

    @Test
    void testJsonDataTypesAsDecimal() throws SQLException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToDecimal.json");
        assertThat(result, table("VARCHAR", "DECIMAL")//
                .row("false", equalTo(null))//
                .row("null", equalTo(null))//
                .row("number", 1.23)//
                .row("string", equalTo(null))//
                .row("true", equalTo(null))//
                .matchesFuzzily());
    }

    @Test
    void testJsonDataTypesAsJson() throws SQLException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToJson.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", null)//
                .row("number", "1.23")//
                .row("string", "\"test\"")//
                .row("true", "true")//
                .matches());
    }

    private ResultSet getDataTypesTestResult(final String mappingFileName) throws SQLException {
        createVirtualSchema(TEST_SCHEMA, mappingFileName);
        uploadDataFileFromResources("dataTypeTests.jsonl");
        return getStatement().executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES ORDER BY TYPE ASC;");
    }

    private void uploadDataFileFromResources(final String resourceName) {
        uploadDataFile(() -> AbstractDocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(IT_RESOURCES + resourceName), resourceName);
    }

    @Test
    void testFilterOnSourceReference() throws SQLException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'testData-1.json'";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertAll(//
                    () -> assertThat(result, table().row("book-1").matches()), //
                    () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post
                    // selection
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                            equalTo("BOOKS.SOURCE_REFERENCE='testData-1.json'"))//
            );
        }
    }

    // @Test //TODO re-enable when SUPPORT-18306 is fixed; See #41
    void testFilterWithOrOnSourceReference() {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA
                + ".BOOKS WHERE SOURCE_REFERENCE = 'testData-1.json' OR SOURCE_REFERENCE = 'testData-2.json' ORDER BY SOURCE_REFERENCE ASC";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                        equalTo("BOOKS.SOURCE_REFERENCE='testData-1.json' OR BOOKS.SOURCE_REFERENCE='testData-2.json'"))//
        );
    }

    /**
     * This test is a workaround for #41 that occurs at {@link #testFilterWithOrOnSourceReference()}
     */
    @Test
    // TODO remove when SUPPORT-18306 is fixed
    void testFilterWithOrOnSourceReferenceWithBugifxForSUPPORT18306() {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM (SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA
                + ".BOOKS WHERE SOURCE_REFERENCE = 'testData-1.json' OR SOURCE_REFERENCE = 'testData-2.json' ORDER BY SOURCE_REFERENCE ASC)";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query), equalTo(
                        "(BOOKS.SOURCE_REFERENCE='testData-1.json') OR (BOOKS.SOURCE_REFERENCE='testData-2.json')"))//
        );
    }

    @Test
    void testFilterOnSourceReferenceForNonExisting() throws SQLException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'UNKNOWN.json'";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertAll(//
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                            equalTo("BOOKS.SOURCE_REFERENCE='UNKNOWN.json'")),
                    () -> assertThat(result, table("VARCHAR").matches()), //
                    () -> assertThat(getPushDownSql(getStatement(), query),
                            equalTo("SELECT * FROM (VALUES (CAST(NULL AS VARCHAR(254)))) WHERE FALSE")));
        }
    }

    @Test
    void testFilterOnSourceReferenceUsingLike() throws SQLException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE LIKE '%1.json'";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertAll(//
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                            equalTo("BOOKS.SOURCE_REFERENCE LIKE '%1.json'")),
                    () -> assertThat(result, table().row("book-1").matches()), //
                    () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE"))// no post selection
            );
        }
    }

    @Test
    void testReadParquetFile(@TempDir final Path tempDir) throws IOException, SQLException {
        createVirtualSchema(TEST_SCHEMA, "mapParquetFile.json");
        final Type idColumn = Types.primitive(INT32, REQUIRED).named("id");
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(tempDir, idColumn);
        parquetTestSetup.writeRow(row -> row.add("id", 123));
        parquetTestSetup.closeWriter();
        final Path parquetFile = parquetTestSetup.getParquetFile();
        try (final FileInputStream parquetStream = new FileInputStream(parquetFile.toFile())) {
            uploadDataFile(() -> parquetStream, "testData-1.parquet");
        }
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertThat(result, table().row("123").matches());
        }
    }

    private void createJsonVirtualSchema() {
        createVirtualSchema(TEST_SCHEMA, "mapJsonFile.json");
        uploadDataFileFromResources("testData-1.json");
        uploadDataFileFromResources("testData-2.json");
    }

    private void createJsonLinesVirtualSchema() {
        createVirtualSchema(TEST_SCHEMA, "mapJsonLinesFile.json");
        uploadDataFileFromResources("test.jsonl");
    }
}
