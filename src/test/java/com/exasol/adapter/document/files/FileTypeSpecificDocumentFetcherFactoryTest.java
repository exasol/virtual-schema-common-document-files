package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;
import com.exasol.adapter.document.mapping.ColumnMapping;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.mapping.TableMapping.Builder;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

class FileTypeSpecificDocumentFetcherFactoryTest {

    private static final FileTypeSpecificDocumentFetcherFactory FACTORY = new FileTypeSpecificDocumentFetcherFactory();

    static Stream<Arguments> getBuiltinTypeCases() {
        return Stream.of(//
                Arguments.of(".json", JsonDocumentFetcher.class),
                Arguments.of(".parquet", ParquetDocumentFetcher.class),
                Arguments.of(".jsonl", JsonLinesDocumentFetcher.class), //
                Arguments.of(".csv", CsvDocumentFetcher.class) //
        );
    }

    @ParameterizedTest
    @MethodSource({ "getBuiltinTypeCases" })
    void testDocumentFetcherBuiltinTypes(final String ending, final Class<?> expectedClass) {
        final FileTypeSpecificDocumentFetcher result = FACTORY.buildFileTypeSpecificDocumentFetcher(ending,
                remoteQuery());
        assertThat(result, Matchers.instanceOf(expectedClass));
    }

    @Test
    void testBuildDocumentFetcherUnsupportedFileType() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> FACTORY.buildFileTypeSpecificDocumentFetcher(".unknown-type", remoteQuery()));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-13: Could not find a file type implementation for '.unknown-type'. Please check the file extension."));
    }

    private RemoteTableQuery remoteQuery(final ColumnMapping... columns) {
        final Builder tableBuilder = TableMapping.rootTableBuilder("destinationName", "remoteName", "additionalConfig");
        Arrays.stream(columns).forEach(tableBuilder::withColumnMappingDefinition);
        return new RemoteTableQuery(tableBuilder.build(), null, null);
    }

    @ParameterizedTest
    @CsvSource({ ".json", ".parquet", ".jsonl", ".csv" })
    void testSchemaFetcherBuiltinTypes(final String ending) {
        final FileTypeSpecificSchemaFetcher result = FACTORY.buildFileTypeSpecificSchemaFetcher(ending);
        assertThat(result, notNullValue());
    }

    @Test
    void testBuildSchemaFetcherUnsupportedFileType() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> FACTORY.buildFileTypeSpecificSchemaFetcher(".unknown-type"));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-13: Could not find a file type implementation for '.unknown-type'. Please check the file extension."));
    }
}