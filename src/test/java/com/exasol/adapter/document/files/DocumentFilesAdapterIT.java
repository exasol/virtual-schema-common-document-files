package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;

@Tag("integration")
@Testcontainers
class DocumentFilesAdapterIT {
    public static final String TEST_SCHEMA = "TEST_SCHEMA";
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFilesAdapterIT.class);
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> container = new ExasolContainer<>()
            .withLogConsumer(new Slf4jLogConsumer(LOGGER)).withReuse(true);
    @TempDir
    static File tempDir;
    private static Connection connection;
    private static Statement statement;

    @BeforeAll
    static void beforeAll() throws Exception {

        connection = container.createConnectionForUser(container.getUsername(), container.getPassword());
        statement = connection.createStatement();
    }

    @AfterAll
    static void afterAll() throws SQLException {
        statement.close();
        connection.close();
    }

    private static Path saveResourceToFile(final String resource) throws IOException {
        final InputStream inputStream = DocumentFilesAdapterIT.class.getClassLoader().getResourceAsStream(resource);
        final Path tempFile = File.createTempFile("resource", "", tempDir).toPath();
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    @AfterEach
    void afterEach() {
        container.purgeDatabase();
    }

    @Test
    void testReadJson()
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final FilesVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new FilesVsExasolTestDatabaseBuilder(
                container);
        final Path mappingFile = saveResourceToFile("mapJsonFile.json");
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile);
        final Path testFile = saveResourceToFile("test.json");
        container.getDefaultBucket().uploadFile(testFile, "test.json");

        final ResultSet resultSet = statement.executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        final List<String> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getString("ID"));
        }
        assertThat(result, containsInAnyOrder("book-1"));
    }

    @Test
    void testReadJsonLines()
            throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        final FilesVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new FilesVsExasolTestDatabaseBuilder(
                container);
        final Path mappingFile = saveResourceToFile("mapJsonLinesFile.json");
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile);
        final Path testFile = saveResourceToFile("test.jsonl");
        container.getDefaultBucket().uploadFile(testFile, "test.jsonl");

        final ResultSet resultSet = statement.executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        final List<String> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getString("ID"));
        }
        assertThat(result, containsInAnyOrder("book-1", "book-2"));
    }

    @Test
    void testJsonDataTypesAsVarcharColumn()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final Map<String, Object> result = getDataTypesTestResult("mapDataTypesToVarchar.json");
        assertAll(//
                () -> assertThat(result.get("number"), equalTo("1.23")),
                () -> assertThat(result.get("null"), equalTo(null)),
                () -> assertThat(result.get("string"), equalTo("test")),
                () -> assertThat(result.get("true"), equalTo("true")),
                () -> assertThat(result.get("false"), equalTo("false"))//
        );
    }

    @Test
    void testJsonDataTypesAsDecimal()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final Map<String, Object> result = getDataTypesTestResult("mapDataTypesToDecimal.json");
        assertAll(//
                () -> assertThat(result.get("number"), equalTo(BigDecimal.valueOf(1.23))),
                () -> assertThat(result.get("null"), equalTo(null)),
                () -> assertThat(result.get("string"), equalTo(null)),
                () -> assertThat(result.get("true"), equalTo(null)),
                () -> assertThat(result.get("false"), equalTo(null))//
        );
    }

    @Test
    void testJsonDataTypesAsJson()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final Map<String, Object> result = getDataTypesTestResult("mapDataTypesToJson.json");
        assertAll(//
                () -> assertThat(result.get("number"), equalTo("1.23")),
                () -> assertThat(result.get("null"), equalTo("null")),
                () -> assertThat(result.get("string"), equalTo("\"test\"")),
                () -> assertThat(result.get("true"), equalTo("true")),
                () -> assertThat(result.get("false"), equalTo("false"))//
        );
    }

    private Map<String, Object> getDataTypesTestResult(final String mappingFileName)
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final FilesVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new FilesVsExasolTestDatabaseBuilder(
                container);
        final Path mappingFile = saveResourceToFile(mappingFileName);
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile);
        final Path testFile = saveResourceToFile("dataTypeTests.jsonl");
        container.getDefaultBucket().uploadFile(testFile, "dataTypeTests.jsonl");

        final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES;");
        final Map<String, Object> result = new HashMap<>();
        while (resultSet.next()) {
            result.put(resultSet.getString("TYPE"), resultSet.getObject("VALUE"));
        }
        return result;
    }
}