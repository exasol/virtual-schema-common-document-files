package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;

class ParquetSchemaFetcherTest {

    @TempDir
    Path tempDir;

    @Test
    void fetchSchema() throws IOException {
        final Type stringColumn = Types.primitive(BINARY, REQUIRED).named("data");
        final Type boolColumn = Types.primitive(BOOLEAN, REQUIRED).named("isActive");
        final Type dateColumn = Types.primitive(INT32, REQUIRED).as(LogicalTypeAnnotation.dateType()).named("my_date");
        final Type timeColumn = Types.primitive(INT32, REQUIRED)
                .as(LogicalTypeAnnotation.timeType(true, LogicalTypeAnnotation.TimeUnit.MILLIS)).named("my_time");
        final Type timestampColumn = Types.primitive(INT64, REQUIRED)
                .as(LogicalTypeAnnotation.timestampType(true, LogicalTypeAnnotation.TimeUnit.MILLIS))
                .named("my_timestamp");
        final Type jsonColumn = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.jsonType()).named("json");

        final Path file = parquetFile(stringColumn, boolColumn, dateColumn, timeColumn, timestampColumn, jsonColumn)
                .closeWriter().getParquetFile();
        final Map<String, MappingDefinition> fields = ((Fields) fetch(file)).getFieldsMap();
        assertThat(fields, aMapWithSize(6));
    }

    @Test
    void fetchEmptyFileFails() throws IOException {
        final Path emptyFile = Files.createFile(this.tempDir.resolve("emptyFile"));
        final RuntimeException exception = assertThrows(RuntimeException.class, () -> fetch(emptyFile));
        assertThat(exception.getMessage(), containsString("is not a Parquet file"));
    }

    private ParquetTestSetup parquetFile(final Type... columnTypes) throws IOException {
        return new ParquetTestSetup(this.tempDir, columnTypes);
    }

    MappingDefinition fetch(final Path file) {
        return new ParquetSchemaFetcher(ColumnNameConverter.upperSnakeCaseConverter())
                .fetchSchema(new RemoteFile(file.toString(), 0, new LocalRemoteFileContent(file))).getMapping();
    }
}
