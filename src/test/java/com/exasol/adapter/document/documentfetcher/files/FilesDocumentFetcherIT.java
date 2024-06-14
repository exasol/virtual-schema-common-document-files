package com.exasol.adapter.document.documentfetcher.files;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;

import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetTestSetup;
import com.exasol.adapter.document.documentnode.*;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.FilesDocumentFetcherFactory;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser.NonWildcardParser;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;

@ExtendWith(MockitoExtension.class)
class FilesDocumentFetcherIT {
    private static final String PATH_FILE_PARQUET = "path/file.parquet";
    @Mock
    FileFinderFactory fileFinderFactoryMock;
    @Mock
    RemoteFileFinder fileFinderMock;
    @TempDir
    Path tempDir;

    @Test
    void parquetAutoInference() throws IOException {
        final StringFilter sourceFilter = new NonWildcardParser().parse(PATH_FILE_PARQUET);
        final ConnectionPropertiesReader properties = new ConnectionPropertiesReader("{}", "user guide url");
        final Path parquetFile = createParquetFile();
        final List<RemoteFile> files = List.of(
                new RemoteFile(PATH_FILE_PARQUET, Files.size(parquetFile), new LocalRemoteFileContent(parquetFile)));
        when(fileFinderMock.loadFiles()).thenAnswer(invocation -> new CloseableIteratorWrapper<>(files.iterator()));
        when(fileFinderFactoryMock.getFinder(same(sourceFilter), same(properties))).thenReturn(fileFinderMock);
        final FileTypeSpecificDocumentFetcher documentFetcher = new ParquetDocumentFetcher();

        final List<DocumentFetcher> fetchers = new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(
                sourceFilter, 0, fileFinderFactoryMock, properties, documentFetcher, "additional Config");
        assertThat(fetchers, hasSize(1));

        final List<FetchedDocument> documents = new ArrayList<>();
        try (CloseableIterator<FetchedDocument> iterator = fetchers.get(0).run(properties)) {
            iterator.forEachRemaining(documents::add);
        }

        assertThat(documents, hasSize(1));
        final DocumentObject node = (DocumentObject) documents.get(0).getRootDocumentNode();
        final Map<String, DocumentNode> row = node.getKeyValueMap();
        assertAll(() -> assertThat(row, aMapWithSize(6)),
                () -> assertThat(((DocumentStringValue) row.get("data")).getValue(), equalTo("my test string")),
                () -> assertThat(((DocumentBooleanValue) row.get("isActive")).getValue(), equalTo(true)),
                () -> assertThat(((DocumentDateValue) row.get("my_date")).getValue().toString(), equalTo("2021-09-23")),
                () -> assertThat(((DocumentDecimalValue) row.get("my_time")).getValue().longValue(), equalTo(1000L)),
                () -> assertThat(((DocumentTimestampValue) row.get("my_timestamp")).getValue(),
                        equalTo(new Timestamp(1632384929000L))),
                () -> assertThat(((DocumentStringValue) row.get("json")).getValue(), equalTo("{\"my_value\": 2}")));
    }

    private Path createParquetFile() throws IOException {
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
        final long timestamp = 1632384929000L;
        parquetTestSetup.writeRow(row -> {
            row.add("data", "my test string");
            row.add("isActive", true);
            row.add("my_date", (int) (timestamp / 24 / 60 / 60 / 1000)); // days since unix-epoch
            row.add("my_time", 1000);// ms after midnight
            row.add("my_timestamp", timestamp);// ms after midnight
            row.add("json", "{\"my_value\": 2}");
        });
        parquetTestSetup.closeWriter();
        return parquetTestSetup.getParquetFile();
    }
}
