package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.JsonLinesDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.parquet.ParquetDocumentFetcher;

class FileTypeSpecificDocumentFetcherFactoryTest {

    private static final FileTypeSpecificDocumentFetcherFactory FACTORY = new FileTypeSpecificDocumentFetcherFactory();

    static Stream<Arguments> getBuiltinTypeCases() {
        return Stream.of(//
                Arguments.of(".json", JsonDocumentFetcher.class),
                Arguments.of(".parquet", ParquetDocumentFetcher.class),
                Arguments.of(".jsonl", JsonLinesDocumentFetcher.class)//
        );
    }

    @ParameterizedTest
    @MethodSource({ "getBuiltinTypeCases" })
    void testBuiltinTypes(final String ending, final Class<?> expectedClass) {
        final FileTypeSpecificDocumentFetcher result = FACTORY.buildFileTypeSpecificDocumentFetcher(ending);
        assertThat(result, Matchers.instanceOf(expectedClass));
    }

    @Test
    void testUnsupportedFileType() {
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> FACTORY.buildFileTypeSpecificDocumentFetcher(".unknown-type"));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-13: Could not find a file type implementation for '.unknown-type'. Please check the file extension."));
    }
}