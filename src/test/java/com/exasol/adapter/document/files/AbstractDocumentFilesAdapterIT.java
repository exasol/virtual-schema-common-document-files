package com.exasol.adapter.document.files;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.udfdebugging.PushDownTesting.getPushDownSql;
import static com.exasol.udfdebugging.PushDownTesting.getSelectionThatIsSentToTheAdapter;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.BINARY;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.parquet.schema.Type;
import org.apache.parquet.schema.Types;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetTestSetup;
import com.exasol.matcher.TypeMatchMode;

@SuppressWarnings("java:S5786") // this class is public so that class from different packages can inherit
public abstract class AbstractDocumentFilesAdapterIT {
    private static final String TEST_SCHEMA = "TEST";
    private static final String IT_RESOURCES = "abstractIntegrationTests/";
    @TempDir
    Path tempDir;
    private String dataFilesDirectory;

    protected abstract Statement getStatement();

    protected abstract void uploadDataFile(final Supplier<InputStream> resource, String resourceName);

    private void createVirtualSchemaWithMappingFromResource(final String schemaName, final String resourceName)
            throws IOException {
        final String mappingTemplate = getMappingTemplate(resourceName);
        final String filledMapping = mappingTemplate.replace("DATA_FILES_DIR", this.dataFilesDirectory);
        createVirtualSchema(schemaName, filledMapping);
    }

    private String getMappingTemplate(final String resourceName) throws IOException {
        try (final InputStream stream = AbstractDocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(IT_RESOURCES + resourceName)) {
            return new String(Objects.requireNonNull(stream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected abstract void createVirtualSchema(String schemaName, String mapping);

    @BeforeEach
    void beforeEach() {
        this.dataFilesDirectory = String.valueOf(System.currentTimeMillis());
    }

    @Test
    void testReadJson() throws SQLException, IOException {
        createJsonVirtualSchema();
        final ResultSet result = getStatement()
                .executeQuery("SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA + ".BOOKS ORDER BY ID ASC;");
        assertThat(result, table().row("book-1", this.dataFilesDirectory + "/testData-1.json")
                .row("book-2", this.dataFilesDirectory + "/testData-2.json").matches());
    }

    @Test
    void testReadJsonLines() throws SQLException, IOException {
        createJsonLinesVirtualSchema();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testJsonDataTypesAsVarcharColumn() throws SQLException, IOException {
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
    void testJsonDataTypesAsDecimal() throws SQLException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToDecimal.json");
        assertThat(result, table("VARCHAR", "DECIMAL")//
                .row("false", equalTo(null))//
                .row("null", equalTo(null))//
                .row("number", 1.23)//
                .row("string", equalTo(null))//
                .row("true", equalTo(null))//
                .matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    void testJsonDataTypesAsJson() throws SQLException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToJson.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", null)//
                .row("number", "1.23")//
                .row("string", "\"test\"")//
                .row("true", "true")//
                .matches());
    }

    private ResultSet getDataTypesTestResult(final String mappingFileName) throws SQLException, IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, mappingFileName);
        uploadDataFileFromResources("dataTypeTests.jsonl");
        return getStatement().executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES ORDER BY TYPE ASC;");
    }

    private void uploadDataFileFromResources(final String resourceName) {
        uploadDataFile(
                () -> AbstractDocumentFilesAdapterIT.class.getClassLoader()
                        .getResourceAsStream(IT_RESOURCES + resourceName),
                this.dataFilesDirectory + "/" + resourceName);
    }

    @Test
    void testFilterOnSourceReference() throws SQLException, IOException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = '"
                + this.dataFilesDirectory + "/testData-1.json'";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertAll(//
                    () -> assertThat(result, table().row("book-1").matches()), //
                    () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post
                    // selection
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                            equalTo("BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory + "/testData-1.json'"))//
            );
        }
    }

    // @Test //TODO re-enable when SPOT-11018 is fixed; See #41
    void testFilterWithOrOnSourceReference() throws IOException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = '"
                + this.dataFilesDirectory + "/testData-1.json' OR SOURCE_REFERENCE = '" + this.dataFilesDirectory
                + "/testData-2.json' ORDER BY SOURCE_REFERENCE ASC";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                        equalTo("BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
                                + "/testData-1.json' OR BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
                                + "testData-2.json'"))//
        );
    }

    /**
     * This test is a workaround for #41 that occurs at {@link #testFilterWithOrOnSourceReference()}
     */
    @Test
    // TODO remove when SPOT-11018 is fixed
    void testFilterWithOrOnSourceReferenceWithBugfixForSPOT11018() throws IOException {
        final String dataFilesDirectory = String.valueOf(System.currentTimeMillis());
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM (SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA
                + ".BOOKS WHERE SOURCE_REFERENCE = '" + dataFilesDirectory + "/testData-1.json' OR SOURCE_REFERENCE = '"
                + dataFilesDirectory + "/testData-2.json' ORDER BY SOURCE_REFERENCE ASC)";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                        equalTo("(BOOKS.SOURCE_REFERENCE='" + dataFilesDirectory
                                + "/testData-1.json') OR (BOOKS.SOURCE_REFERENCE='" + dataFilesDirectory
                                + "/testData-2.json')"))//
        );
    }

    @Test
    void testFilterOnSourceReferenceForNonExisting() throws SQLException, IOException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'UNKNOWN.json'";
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertAll(//
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                            equalTo("BOOKS.SOURCE_REFERENCE='UNKNOWN.json'")),
                    () -> assertThat(result, table("VARCHAR").matches()), //
                    () -> assertThat(getPushDownSql(getStatement(), query),
                            equalTo("SELECT * FROM (VALUES (CAST(NULL AS  VARCHAR(254)))) WHERE FALSE")));
        }
    }

    @Test
    void testFilterOnSourceReferenceUsingLike() throws SQLException, IOException {
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

    protected abstract void uploadDataFile(final Path file, String resourceName);

    static Stream<Arguments> testLargeParquetFiles() {
        final int k = 1000;
        final int m = k * k;
        return Stream.of(//
                Arguments.of(k, m, 1), //
                Arguments.of(m, k, 1), //
                Arguments.of(k, k, 10)//
        );
    }

    @Test
    void testReadParquetFile() throws IOException, SQLException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapParquetFile.json");
        final Type idColumn = Types.primitive(BINARY, REQUIRED).named("data");
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, idColumn);
        parquetTestSetup.writeRow(row -> row.add("data", "my test string"));
        parquetTestSetup.closeWriter();
        uploadAsParquetFile(parquetTestSetup, 1);
        final String query = "SELECT \"DATA\" FROM " + TEST_SCHEMA + ".BOOKS";
        assertQuery(query, table().row("my test string").matches());
    }

    @ParameterizedTest
    @MethodSource("testLargeParquetFiles")
    @Tag("regression")
    void testLargeParquetFiles(final int itemSize, final long itemCount, final int fileCount, final TestInfo testInfo)
            throws Exception {
        final Random random = new Random(1);
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapParquetFile.json");
        for (int fileCounter = 0; fileCounter < fileCount; fileCounter++) {
            final Type idColumn = Types.primitive(BINARY, REQUIRED).named("data");
            final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, idColumn);
            for (long counter = 0; counter < itemCount; counter++) {
                final byte[] data = new byte[itemSize];
                random.nextBytes(data);
                parquetTestSetup.writeRow(row -> {
                    row.add("data", new String(data, StandardCharsets.US_ASCII));
                });
            }
            parquetTestSetup.closeWriter();
            uploadAsParquetFile(parquetTestSetup, fileCounter);
        }
        final String query = "SELECT COUNT(*) FROM " + TEST_SCHEMA + ".BOOKS";
        new PerformanceTestLogger(testInfo)
                .profile(() -> assertQuery(query, table().row(itemCount * fileCount).matches()));
    }

    private void uploadAsParquetFile(final ParquetTestSetup parquetTestSetup, final int fileCount) throws IOException {
        final Path parquetFile = parquetTestSetup.getParquetFile();
        uploadDataFile(parquetFile, this.dataFilesDirectory + "/testData-" + fileCount + ".parquet");
    }

    private void assertQuery(final String query, final Matcher<ResultSet> matcher) throws SQLException {
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertThat(result, matcher);
        }
    }

    private static class PerformanceTestLogger {
        private static final Logger LOGGER = Logger.getLogger(PerformanceTestLogger.class.getName());
        private final TestInfo testInfo;

        private PerformanceTestLogger(final TestInfo testInfo) {
            this.testInfo = testInfo;
        }

        void profile(final RunnableWithException methodUnderTest) throws Exception {
            final String testName = this.testInfo.getDisplayName();
            LOGGER.log(Level.INFO, "Starting stopwatch for {0}", new Object[] { testName });
            final long startMillis = System.currentTimeMillis();
            methodUnderTest.run();
            final long duration = System.currentTimeMillis() - startMillis;
            LOGGER.log(Level.INFO, "{0} took {1} ms", new Object[] { testName, duration });
        }

        interface RunnableWithException {
            void run() throws Exception;
        }
    }

    private void createJsonVirtualSchema() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapJsonFile.json");
        uploadDataFileFromResources("testData-1.json");
        uploadDataFileFromResources("testData-2.json");
    }

    private void createJsonLinesVirtualSchema() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapJsonLinesFile.json");
        uploadDataFileFromResources("test.jsonl");
    }
}
