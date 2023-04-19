package com.exasol.adapter.document.files;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.udfdebugging.PushDownTesting.getPushDownSql;
import static com.exasol.udfdebugging.PushDownTesting.getSelectionThatIsSentToTheAdapter;
import static java.util.stream.Collectors.joining;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.parquet.schema.LogicalTypeAnnotation;
import org.apache.parquet.schema.Type;
import org.apache.parquet.schema.Types;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.documentfetcher.files.csv.CsvTestSetup;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetTestSetup;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.EdmlDefinition.EdmlDefinitionBuilder;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.matcher.TypeMatchMode;
import com.exasol.performancetestrecorder.PerformanceTestRecorder;

@SuppressWarnings("java:S5786") // this class is public so that class from different packages can inherit
public abstract class AbstractDocumentFilesAdapterIT {
    private static final String TEST_SCHEMA = "TEST";
    private static final String IT_RESOURCES = "abstractIntegrationTests/";
    private static final Logger LOGGER = Logger.getLogger(AbstractDocumentFilesAdapterIT.class.getName());
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

    private void createVirtualSchemaWithMapping(final String schemaName, final Fields mapping,
            final String dataFilePattern) throws IOException {
        final EdmlDefinitionBuilder edmlDefinition = defaultEdml(mapping, dataFilePattern);
        createVirtualSchemaWithMapping(schemaName, edmlDefinition);
    }

    private EdmlDefinitionBuilder csvEdmlWithHeader(final Fields mapping, final String dataFilePattern) {
        return defaultEdml(mapping, dataFilePattern).additionalConfiguration("{\"csv-headers\": true}");
    }

    private EdmlDefinitionBuilder csvEdmlWithoutHeader(final Fields mapping, final String dataFilePattern) {
        return defaultEdml(mapping, dataFilePattern).additionalConfiguration("{\"csv-headers\": false}");
    }

    private EdmlDefinitionBuilder defaultEdml(final Fields mapping, final String dataFilePattern) {
        return EdmlDefinition.builder() //
                .destinationTable("BOOKS") //
                .additionalConfiguration("") //
                .addSourceReferenceColumn(true) //
                .mapping(mapping) //
                .source(this.dataFilesDirectory + "/" + dataFilePattern);
    }

    private void createVirtualSchemaWithMapping(final String schemaName, final Fields mapping,
            final String dataFilePattern, final String additionalConfiguration) throws IOException {
        final EdmlDefinitionBuilder edmlDefinition = EdmlDefinition.builder() //
                .destinationTable("BOOKS").additionalConfiguration(additionalConfiguration) //
                .addSourceReferenceColumn(true) //
                .mapping(mapping) //
                .source(this.dataFilesDirectory + "/" + dataFilePattern);
        createVirtualSchemaWithMapping(schemaName, edmlDefinition);
    }

    private void createVirtualSchemaWithMapping(final String schemaName, final EdmlDefinitionBuilder edmlDefinition) {
        final String edmlString = new EdmlSerializer().serialize(edmlDefinition.build());
        LOGGER.fine(() -> "Creating virtual schema '" + schemaName + "' using EDML '" + edmlString + "'");
        final Instant start = Instant.now();
        createVirtualSchema(schemaName, edmlString);
        LOGGER.fine(() -> "Virtual schema '" + schemaName + "' created in " + Duration.between(start, Instant.now()));
    }

    private String getMappingTemplate(final String resourceName) throws IOException {
        try (final InputStream stream = AbstractDocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(IT_RESOURCES + resourceName)) {
            return new String(Objects.requireNonNull(stream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Create a virtual schema in the test database.
     *
     * @param schemaName name of the virtual schema
     * @param mapping    mapping file content
     */
    protected abstract void createVirtualSchema(String schemaName, String mapping);

    @BeforeEach
    void beforeEach(final TestInfo testInfo) {
        LOGGER.log(Level.INFO, "Start... {0}", testInfo.getDisplayName());
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
    @Tag("regression")
    void testReadFewJsonFiles(final TestInfo testInfo) throws Exception {
        createJsonVirtualSchema();
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                final ResultSet result = getStatement()
                        .executeQuery("SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA + ".BOOKS ORDER BY ID ASC;");
                assertThat(result, table().row("book-1", this.dataFilesDirectory + "/testData-1.json")
                        .row("book-2", this.dataFilesDirectory + "/testData-2.json").matches());
            });
        }
    }

    @Test
    void testReadJsonLines() throws SQLException, IOException {
        createJsonLinesVirtualSchema();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testReadCsv() throws SQLException, IOException {
        createCsvVirtualSchema();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testReadCsvHeaders() throws SQLException, IOException {
        createCsvVirtualSchemaHeaders();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testReadCsvNoHeaders() throws SQLException, IOException {
        createCsvVirtualSchemaNoHeaders();
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testCsvWithTypes() throws IOException, SQLException {
        final Fields mapping = Fields.builder()//
                .mapField("str", ToVarcharMapping.builder().build()) //
                .mapField("bool", ToBoolMapping.builder().destinationName("IS_ACTIVE").build())//
                .mapField("float_col", ToDecimalMapping.builder().decimalPrecision(10).decimalScale(5).build()) //
                .mapField("int_col", ToDecimalMapping.builder().decimalPrecision(5).decimalScale(0).build()) //
                .mapField("date_col", ToDateMapping.builder().build()) //
                .mapField("timestamp_col", ToTimestampMapping.builder().build()) //
                .build();
        final EdmlDefinitionBuilder edmlDefinition = csvEdmlWithHeader(mapping, "testData-*.csv");
        createVirtualSchemaWithMapping(TEST_SCHEMA, edmlDefinition);

        uploadFileContent("testData-1.csv", List.of("str, bool, float_col, int_col, date_col, timestamp_col", //
                "\"test\", true, 1.23, 42, 2007-12-03, 2007-12-03T10:15:30.00Z",
                "test2, FALSE, 1.23e-4, -17, 2007-12-03, 2007-12-03T10:15:30.00Z",
                "null, null, null, null, null, null"));
        final String query = "SELECT str, IS_ACTIVE, float_col, int_col, date_col, timestamp_col FROM " + TEST_SCHEMA
                + ".BOOKS";
        assertQuery(query, table("VARCHAR", "BOOLEAN", "DECIMAL", "INTEGER", "DATE", "TIMESTAMP") //
                .row("test", true, 1.23, 42, Date.valueOf("2007-12-03"), Timestamp.valueOf("2007-12-03 10:15:30.00"))
                .row("test2", true, 1.23e-4, -17, Date.valueOf("2007-12-03"),
                        Timestamp.valueOf("2007-12-03 10:15:30.00"))
                .row(null, null, null, null, null, null) //
                .withUtcCalendar().matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    @Tag("regression")
    void testReadSmallJsonLines(final TestInfo testInfo) throws Exception {
        createJsonLinesVirtualSchema();
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
                assertThat(result, table().row("book-1").row("book-2").matches());
            });
        }
    }

    @Test
    void testJsonDataTypesAsVarcharColumn() throws SQLException, IOException {
        final ResultSet result = getJsonDataTypesTestResult("mapDataTypesToVarchar.json");
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
        final ResultSet result = getJsonDataTypesTestResult("mapDataTypesToDecimal.json");
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
        final ResultSet result = getJsonDataTypesTestResult("mapDataTypesToJson.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", null)//
                .row("number", "1.23")//
                .row("string", "\"test\"")//
                .row("true", "true")//
                .matches());
    }

    // everything in a csv is currently read out into a stringholdernode,
    // this is a problem when using anything else than toVarcharmapping in the edml definition
    // (toBoolMapping,toDecimalMapping, ...)
    // the workaround currently is to use SQL conversion functions in the query itself
    @Test
    void testCsvDataTypesConversion() throws SQLException, IOException {
        final ResultSet result = getCsvDataTypesTestResult("mapCsvToVarchar.json", "dataTypeTests.csv",
                "SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES");
        assertThat(result,
                table("VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR")
                        .row("1.23", "null", "test", "true", "false")//
                        .matches());
    }

    @Test
    void testCsvDataTypesConversionWorkaround() throws SQLException, IOException {
        final ResultSet result = getCsvDataTypesTestResult("mapCsvToVarcharWorkaround.json",
                "dataTypeWorkaroundTests.csv",
                "SELECT CONVERT( BOOLEAN, BOOLEANCOLUMN ) CONVERTEDBOOLEAN FROM " + TEST_SCHEMA + ".DATA_TYPES");
        assertThat(result, table("BOOLEAN").row(false)//
                .matches());
    }

    private ResultSet getJsonDataTypesTestResult(final String mappingFileName) throws SQLException, IOException {
        final String dataFile = "dataTypeTests.jsonl";
        return getDatatypesTestResult(mappingFileName, dataFile);
    }

    private ResultSet getDatatypesTestResult(final String mappingFileName, final String dataFile)
            throws IOException, SQLException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, mappingFileName);
        uploadDataFileFromResources(dataFile);
        return getStatement().executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES ORDER BY TYPE ASC;");
    }

    private ResultSet getCsvDataTypesTestResult(final String mappingFileName, final String dataFile,
            final String sqlQuery) throws SQLException, IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, mappingFileName);
        uploadDataFileFromResources(dataFile);
        return getStatement().executeQuery(sqlQuery);
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

    /**
     * SPOT-11018 (fixed) https://github.com/exasol/virtual-schema-common-document-files/issues/41
     */
    @Test
    void testFilterWithOrOnSourceReference() throws IOException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = '"
                + this.dataFilesDirectory + "/testData-1.json' OR SOURCE_REFERENCE = '" + this.dataFilesDirectory
                + "/testData-2.json' ORDER BY SOURCE_REFERENCE ASC";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                        equalTo("(BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
                                + "/testData-1.json') OR (BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
                                + "/testData-2.json')"))//
        );
    }

    /**
     * This test is a workaround for #41 that occurs at {@link #testFilterWithOrOnSourceReference()} SPOT-11018
     * https://github.com/exasol/virtual-schema-common-document-files/issues/41
     */
    @Test
    // workaround for
    void testFilterWithOrOnSourceReferenceWithBugfixForSPOT11018() throws IOException {
        createJsonVirtualSchema();
        final String query = "SELECT ID FROM (SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA
                + ".BOOKS WHERE SOURCE_REFERENCE = '" + this.dataFilesDirectory
                + "/testData-1.json' OR SOURCE_REFERENCE = '" + this.dataFilesDirectory
                + "/testData-2.json' ORDER BY SOURCE_REFERENCE ASC)";
        assertAll(//
                () -> assertThat(getStatement().executeQuery(query), table().row("book-1").row("book-2").matches()), //
                () -> assertThat(getPushDownSql(getStatement(), query), endsWith("WHERE TRUE")), // no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(getStatement(), query),
                        equalTo("(BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
                                + "/testData-1.json') OR (BOOKS.SOURCE_REFERENCE='" + this.dataFilesDirectory
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

    @Test
    void testReadParquetFile() throws IOException, SQLException {
        final Fields mapping = Fields.builder()//
                .mapField("data", ToVarcharMapping.builder().build())//
                .mapField("isActive", ToBoolMapping.builder().destinationName("IS_ACTIVE").build())//
                .mapField("my_date", ToDateMapping.builder().build())//
                .mapField("my_time", ToDecimalMapping.builder().decimalPrecision(15).decimalScale(0).build())//
                .mapField("my_timestamp", ToTimestampMapping.builder().build())//
                .mapField("json", ToVarcharMapping.builder().build())//
                .build();
        createVirtualSchemaWithMapping(TEST_SCHEMA, mapping, "testData-*.parquet");
        final Type stringColumn = Types.primitive(BINARY, REQUIRED).named("data");
        final Type boolColumn = Types.primitive(BOOLEAN, REQUIRED).named("isActive");
        final Type dateColumn = Types.primitive(INT32, REQUIRED).as(LogicalTypeAnnotation.dateType()).named("my_date");
        final Type timeColumn = Types.primitive(INT32, REQUIRED)
                .as(LogicalTypeAnnotation.timeType(true, LogicalTypeAnnotation.TimeUnit.MILLIS)).named("my_time");
        final Type timestampColumn = Types.primitive(INT64, REQUIRED)
                .as(LogicalTypeAnnotation.timestampType(true, LogicalTypeAnnotation.TimeUnit.MILLIS))
                .named("my_timestamp");
        final Type jsonColumn = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.jsonType()).named("json");
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, stringColumn, boolColumn,
                dateColumn, timeColumn, timestampColumn, jsonColumn);
        final long a_timestamp = 1632384929000L;
        parquetTestSetup.writeRow(row -> {
            row.add("data", "my test string");
            row.add("isActive", true);
            row.add("my_date", (int) (a_timestamp / 24 / 60 / 60 / 1000)); // days since unix-epoch
            row.add("my_time", 1000);// ms after midnight
            row.add("my_timestamp", a_timestamp);// ms after midnight
            row.add("json", "{\"my_value\": 2}");
        });
        parquetTestSetup.closeWriter();
        uploadAsParquetFile(parquetTestSetup.getParquetFile(), 1);
        final String query = "SELECT \"DATA\", \"IS_ACTIVE\", \"MY_DATE\", \"MY_TIME\", \"MY_TIMESTAMP\", \"JSON\" FROM "
                + TEST_SCHEMA + ".BOOKS";
        assertQuery(query, table().row("my test string", true, new Date(a_timestamp), 1000, new Timestamp(a_timestamp),
                "{\"my_value\": 2}").withUtcCalendar().matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    void testReadParquetFileWithAutomaticInference() throws IOException, SQLException {
        final Type stringColumn = Types.primitive(BINARY, REQUIRED).named("data");
        final Type boolColumn = Types.primitive(BOOLEAN, REQUIRED).named("isActive");
        final Type dateColumn = Types.primitive(INT32, REQUIRED).as(LogicalTypeAnnotation.dateType()).named("my_date");
        final Type timeColumn = Types.primitive(INT32, REQUIRED)
                .as(LogicalTypeAnnotation.timeType(true, LogicalTypeAnnotation.TimeUnit.MILLIS)).named("my_time");
        final Type timestampColumn = Types.primitive(INT64, REQUIRED)
                .as(LogicalTypeAnnotation.timestampType(true, LogicalTypeAnnotation.TimeUnit.MILLIS))
                .named("my_timestamp");
        final Type jsonColumn = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.jsonType()).named("json");
        final long a_timestamp = 1632384929000L;
        uploadAsParquetFile(parquetFile(stringColumn, boolColumn, dateColumn, timeColumn, timestampColumn, jsonColumn) //
                .writeRow(row -> {
                    row.add("data", "my test string");
                    row.add("isActive", true);
                    row.add("my_date", (int) (a_timestamp / 24 / 60 / 60 / 1000)); // days since unix-epoch
                    row.add("my_time", 1000);// ms after midnight
                    row.add("my_timestamp", a_timestamp);// ms after midnight
                    row.add("json", "{\"my_value\": 2}");
                }).closeWriter(), 1);

        final String source = this.dataFilesDirectory + "/testData-*.parquet";
        createVirtualSchemaWithMapping(TEST_SCHEMA, EdmlDefinition.builder().source(source).destinationTable("BOOKS"));

        final String query = "SELECT \"DATA\", \"IS_ACTIVE\", \"MY_DATE\", \"MY_TIME\", \"MY_TIMESTAMP\", \"JSON\" FROM "
                + TEST_SCHEMA + ".BOOKS";
        assertQuery(query, table().row("my test string", true, new Date(a_timestamp), 1000, new Timestamp(a_timestamp),
                "{\"my_value\": 2}").withUtcCalendar().matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    void testReadMultipleParquetFilesWithAutomaticInference() throws IOException, SQLException {
        final Type stringColumn = Types.primitive(BINARY, REQUIRED).named("data");
        uploadAsParquetFile(parquetFile(stringColumn).writeRow(row -> row.add("data", "row1")).closeWriter(), 1);
        uploadAsParquetFile(parquetFile(stringColumn).writeRow(row -> row.add("data", "row2")).closeWriter(), 2);

        final String source = this.dataFilesDirectory + "/testData-*.parquet";
        createVirtualSchemaWithMapping(TEST_SCHEMA,
                EdmlDefinition.builder().source(source).destinationTable("BOOKS").addSourceReferenceColumn(true));

        final String query = "SELECT \"DATA\" FROM " + TEST_SCHEMA + ".BOOKS ORDER BY SOURCE_REFERENCE";
        assertQuery(query, table().row("row1").row("row2").withUtcCalendar().matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
    }

    @Test
    void testReadParquetFileWithAutomaticInferenceMissingInputFile() throws IOException, SQLException {
        final String source = this.dataFilesDirectory + "/file-does-not-exist-*.parquet";
        final EdmlDefinitionBuilder edmlDefinition = EdmlDefinition.builder().source(source).destinationTable("BOOKS");
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createVirtualSchemaWithMapping(TEST_SCHEMA, edmlDefinition));
        assertThat(exception.getCause().getMessage(),
                containsString(
                        "E-VSD-102: Schema auto inference for source '" + source + "' failed. Known mitigations:\n"
                                + "* Make sure that the input files exist at '" + source + "'"));
    }

    @Test
    void testReadJsonFileWithAutomaticInferenceNotSupported() throws IOException, SQLException {
        final String source = this.dataFilesDirectory + "/auto-inference-unsupported-*.json";
        final EdmlDefinitionBuilder edmlDefinition = EdmlDefinition.builder().source(source).destinationTable("BOOKS");
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createVirtualSchemaWithMapping(TEST_SCHEMA, edmlDefinition));
        assertThat(exception.getCause().getMessage(),
                containsString("E-VSD-101: This virtual schema does not support auto inference for source '" + source
                        + "'. Please specify the 'mapping' element in the JSON EDML definition."));
    }

    @Test
    @Tag("regression")
    void testLoadManyParquetRowsFromOneFile(final TestInfo testInfo) throws Exception {
        prepareAndRunParquetLoadingTest(1_000, 1_000_000, 1, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadManyParquetRows(final TestInfo testInfo) throws Exception {
        prepareAndRunParquetLoadingTest(100, 1_000_000, 10, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadLargeParquetRows(final TestInfo testInfo) throws Exception {
        prepareAndRunParquetLoadingTest(1_000_000, 100, 10, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadManyParquetColumns(final TestInfo testInfo) throws Exception {
        prepareAndRunParquetLoadingTest(1_000, 1_000, 10, 100, testInfo);
    }

    void prepareAndRunParquetLoadingTest(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount, final TestInfo testInfo) throws Exception {
        prepareParquetLoadingTest(itemSize, rowCount, fileCount, columnCount);
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            runSingleParquetLoadingTest(rowCount, fileCount, testInfo);
        }
    }

    private ParquetTestSetup parquetFile(final Type... columnTypes) throws IOException {
        return new ParquetTestSetup(this.tempDir, columnTypes);
    }

    private void prepareParquetLoadingTest(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount) throws IOException {
        final Random random = new Random(1);
        final Fields.FieldsBuilder fieldsBuilder = Fields.builder();
        for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
            fieldsBuilder.mapField("data" + columnCounter,
                    ToVarcharMapping.builder().varcharColumnSize(2_000_000).build());
        }
        final Fields mapping = fieldsBuilder.build();
        createVirtualSchemaWithMapping(TEST_SCHEMA, mapping, "testData-*.parquet");
        for (int fileCounter = 0; fileCounter < fileCount; fileCounter++) {
            LOGGER.info("started creating parquet file");
            final Path parquetFile = createParquetFile(itemSize, rowCount, columnCount, random);
            LOGGER.info("done creating; uploading...");
            uploadAsParquetFile(parquetFile, fileCounter);
            LOGGER.info("done uploading");
        }
    }

    private void runSingleParquetLoadingTest(final long rowCount, final int fileCount, final TestInfo testInfo)
            throws Exception {
        final String query = "SELECT COUNT(*) FROM " + TEST_SCHEMA + ".BOOKS";
        PerformanceTestRecorder.getInstance().recordExecution(testInfo,
                () -> assertQuery(query, table().row(rowCount * fileCount).matches()));
    }

    private Path createParquetFile(final int itemSize, final long rowCount, final int columnCount, final Random random)
            throws IOException {
        final List<Type> columns = createParquetColumnDefinitions(columnCount);
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, columns.toArray(Type[]::new));
        for (long rowCounter = 0; rowCounter < rowCount; rowCounter++) {
            final byte[] data = new byte[itemSize];
            parquetTestSetup.writeRow(row -> {
                for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
                    random.nextBytes(data);
                    final String columnName = "data" + columnCounter;
                    row.add(columnName, new String(data, StandardCharsets.US_ASCII));
                }
            });
        }
        parquetTestSetup.closeWriter();
        return parquetTestSetup.getParquetFile();
    }

    private List<Type> createParquetColumnDefinitions(final int columnCount) {
        final List<Type> columns = new ArrayList<>(columnCount);
        for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
            columns.add(Types.primitive(BINARY, REQUIRED).named("data" + columnCounter));
        }
        return columns;
    }

    @Test
    void testLoadCsvRows(final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTestNoMeasure(100, 100, 10, 10, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadManyCsvRowsFromOneFile(final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(1_000, 1_000_000, 1, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadManyCsvRows(final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(100, 1_000_000, 10, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadLargeCsvRows(final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(1_000_000, 100, 10, 1, testInfo);
    }

    @Test
    @Tag("regression")
    void testLoadManyCsvColumns(final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(1_000, 1_000, 10, 100, testInfo);
    }

    void prepareAndRunCsvLoadingTest(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount, final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(itemSize, rowCount, fileCount, columnCount, testInfo, true);
    }

    void prepareAndRunCsvLoadingTestNoMeasure(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount, final TestInfo testInfo) throws Exception {
        prepareAndRunCsvLoadingTest(itemSize, rowCount, fileCount, columnCount, testInfo, false);
    }

    void prepareAndRunCsvLoadingTest(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount, final TestInfo testInfo, final boolean measure) throws Exception {
        prepareCsvLoadingTest(itemSize, rowCount, fileCount, columnCount);
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            runSingleCsvLoadingTest(rowCount, fileCount, testInfo, measure);
        }
    }

    private void prepareCsvLoadingTest(final int itemSize, final long rowCount, final int fileCount,
            final int columnCount) throws IOException {
        final Random random = new Random(1);
        final Fields.FieldsBuilder fieldsBuilder = Fields.builder();
        for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
            fieldsBuilder.mapField("data" + columnCounter,
                    ToVarcharMapping.builder().varcharColumnSize(2_000_000).build());
        }
        final Fields mapping = fieldsBuilder.build();
        final String additionalConfiguration = "{\n" + "    \"csv-headers\": true\n" + "  }";
        createVirtualSchemaWithMapping(TEST_SCHEMA, mapping, "testData-*.csv", additionalConfiguration);
        for (int fileCounter = 0; fileCounter < fileCount; fileCounter++) {
            LOGGER.info("started creating CSV file");
            final Path csvFile = createCsvFile(itemSize, rowCount, columnCount, random);
            LOGGER.info("done creating; uploading...");
            uploadAsCsvFile(csvFile, fileCounter);
            LOGGER.info("done uploading");
        }
    }

    private void runSingleCsvLoadingTest(final long rowCount, final int fileCount, final TestInfo testInfo,
            final boolean measure) throws Exception {
        final String query = "SELECT COUNT(*) FROM " + TEST_SCHEMA + ".BOOKS";
        if (measure) {
            PerformanceTestRecorder.getInstance().recordExecution(testInfo,
                    () -> assertQuery(query, table().row(rowCount * fileCount).matches()));
        } else {
            assertQuery(query, table().row(rowCount * fileCount).matches());
        }
    }

    private Path createCsvFile(final int itemSize, final long rowCount, final int columnCount, final Random random)
            throws IOException {
        final List<String> columns = createCsvColumnDefinitions(columnCount);
        final CsvTestSetup csvTestSetup = new CsvTestSetup(this.tempDir, columns);
        for (long rowCounter = 0; rowCounter < rowCount; rowCounter++) {
            final List<String> row = new ArrayList<>();
            final byte[] data = new byte[itemSize];
            for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
                random.nextBytes(data);
                // final String columnName = "data" + columnCounter;
                row.add(new String(data, StandardCharsets.US_ASCII));
            }
            csvTestSetup.writeRow(row);
        }
        csvTestSetup.closeWriter();
        return csvTestSetup.getCsvFile();
    }

    private List<String> createCsvColumnDefinitions(final int columnCount) {
        final List<String> columns = new ArrayList<>(columnCount);
        for (int columnCounter = 0; columnCounter < columnCount; columnCounter++) {
            columns.add("data" + columnCounter);
        }
        return columns;
    }

    @Test
    void testOverrideFileType() throws IOException, SQLException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapJsonLinesFileWithStrangeExtension.json");
        uploadDataFile(
                () -> AbstractDocumentFilesAdapterIT.class.getClassLoader()
                        .getResourceAsStream(IT_RESOURCES + "test.jsonl"),
                this.dataFilesDirectory + "/" + "test.strange-extension");
        final ResultSet result = getStatement().executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    private void uploadAsParquetFile(final ParquetTestSetup parquetFile, final int fileCount) {
        uploadAsParquetFile(parquetFile.getParquetFile(), fileCount);
    }

    private void uploadAsParquetFile(final Path parquetFile, final int fileCount) {
        uploadDataFile(parquetFile, this.dataFilesDirectory + "/testData-" + fileCount + ".parquet");
    }

    private void uploadAsCsvFile(final Path csvFile, final int fileCount) {
        uploadDataFile(csvFile, this.dataFilesDirectory + "/testData-" + fileCount + ".csv");
    }

    private void uploadFileContent(final String resourceName, final List<String> content) {
        uploadFileContent(resourceName, content.stream().collect(joining("\n")));
    }

    private void uploadFileContent(final String resourceName, final String content) {
        try {
            final Path tempFile = Files.createTempFile(this.tempDir, "upload-content", ".data");
            Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
            uploadDataFile(tempFile, this.dataFilesDirectory + "/" + resourceName);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    protected abstract void uploadDataFile(final Path file, String resourceName);

    private void assertQuery(final String query, final Matcher<ResultSet> matcher) throws SQLException {
        LOGGER.finest(() -> "Executing query '" + query + "'");
        final Instant start = Instant.now();
        try (final ResultSet result = getStatement().executeQuery(query)) {
            assertThat(result, matcher);
        }
        LOGGER.fine(() -> "Executed query in " + Duration.between(start, Instant.now()));
    }

    protected void createJsonVirtualSchema() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapJsonFile.json");
        uploadDataFileFromResources("testData-1.json");
        uploadDataFileFromResources("testData-2.json");
    }

    private void createJsonLinesVirtualSchema() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapJsonLinesFile.json");
        uploadDataFileFromResources("test.jsonl");
    }

    private void createCsvVirtualSchema() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapCsvFile.json");
        uploadDataFileFromResources("test.csv");
    }

    private void createCsvVirtualSchemaNoHeaders() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapCsvFileAdditionalConfigurationNoHeaders.json");
        uploadDataFileFromResources("testCsvNoHeaders.csv");
    }

    private void createCsvVirtualSchemaHeaders() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapCsvFileAdditionalConfigurationHeaders.json");
        uploadDataFileFromResources("testCsvHeaders.csv");
    }

    private void createCsvVirtualSchemaDifferentDelimiter() throws IOException {
        createVirtualSchemaWithMappingFromResource(TEST_SCHEMA, "mapCsvFileDifferentDelimiter.json");
        uploadDataFileFromResources("testCsvDifferentDelimiter.csv");
    }
}
