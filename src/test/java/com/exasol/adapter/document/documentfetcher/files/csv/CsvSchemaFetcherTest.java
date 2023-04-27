package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.testutil.MappingMatchers.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;

class CsvSchemaFetcherTest {

    @Test
    void testEmptyInput() {
        final List<String> emptyCsv = List.of();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> fetchSchema(emptyCsv));
        assertThat(exception.getMessage(), equalTo("E-VSDF-70: Failed to read CSV file 'resourceName'"));
        assertThat(exception.getCause().getMessage(),
                equalTo("Can't proceed because input file is empty and client has not specified headers"));
    }

    @Test
    void testVarcharColumn() {
        assertColumnTypeDetected(List.of("val0", "val1", "val2"), false, varcharMapping(varcharColumnsSize(2_000_000)));
    }

    @Test
    void testQuotedNumberInterpretedAsVarcharColumn() {
        assertColumnTypeDetected(List.of("'1'"), false, varcharMapping());
    }

    @Test
    void testDoubleQuotedNumberInterpretedAsDecimalColumn() {
        assertColumnTypeDetected(List.of("\"1\""), false, decimalMapping());
    }

    @Test
    void testCharColumn() {
        assertColumnTypeDetected(List.of("a", "b", "c"), false, varcharMapping(varcharColumnsSize(1)));
    }

    @ParameterizedTest
    @CsvSource({ "true", "false", "TRUE", "FALSE", "True", "False" })
    void testBooleanColumn(final String value) {
        assertColumnTypeDetected(List.of(value), false, boolMapping());
        assertColumnTypeDetected(List.of("header", value), true, boolMapping());
    }

    @ParameterizedTest
    @CsvSource({ "1", "-1", "0", //
            "2147483647", // max int
            "-2147483648" // min int
    })
    void testIntColumn(final String value) {
        assertColumnTypeDetected(List.of(value), false, decimalMapping(precision(10), scale(0)));
        assertColumnTypeDetected(List.of("header", value), true, decimalMapping(precision(10), scale(0)));
    }

    @ParameterizedTest
    @CsvSource({ "2147483648", // max int + 1
            "-2147483649", // min int - 1
            "9223372036854775807", // max long
            "-9223372036854775808" // min long
    })
    void testLongColumn(final String value) {
        assertColumnTypeDetected(List.of(value), false, decimalMapping(precision(20), scale(0)));
        assertColumnTypeDetected(List.of("header", value), true, decimalMapping(precision(20), scale(0)));
    }

    @ParameterizedTest
    @CsvSource({ "1.2", "0.1", "0.0", "1.2e7", "1.2e-7", ".3", ".4e7", ".6e-9", ".6e+9", })
    void testDoubleColumn(final String value) {
        assertColumnTypeDetected(List.of(value), false, doubleMapping());
        assertColumnTypeDetected(List.of("header", value), true, doubleMapping());
    }

    @ParameterizedTest
    @CsvSource({ "2023-04-25", "23-04-25", "25.4.2023", })
    void testDateColumnConvertedToVarchar(final String value) {
        assertColumnTypeDetected(List.of(value), false, varcharMapping());
        assertColumnTypeDetected(List.of("header", value), false, varcharMapping());
    }

    @ParameterizedTest
    @CsvSource({ "2023-04-25 10:25:42", "2023-04-25 10:25:42.1234", "2023-04-25T10:25:42Z", "2023-04-25T10:25:42.1234Z",
            "2023-04-25 10:25:42Z", "2023-04-25 10:25:42.1234Z", "25.04.2023 10:25:42", "2023-04-25T10:25:42+001",
            "2023-04-25T10:25:42 Europe/Berlin", "2007-12-03T10:15:30+01:00[Europe/Paris]", })
    void testTimestampColumnConvertedToVarchar(final String value) {
        assertColumnTypeDetected(List.of("header", value), false, varcharMapping());
        assertColumnTypeDetected(List.of(value), false, varcharMapping());
    }

    @Test
    void testMultipleColumns() {
        final Fields fields = fetchSchema("val0,0,1.1", "val1,1,2.2", "val2,2,3.3");
        assertThat(fields.getFieldsMap(), allOf( //
                aMapWithSize(3), //
                hasEntry(equalTo("0"),
                        allOf(columnMapping(destinationName("COLUMN_0")), varcharMapping(varcharColumnsSize(2000000)))),
                hasEntry(equalTo("1"),
                        allOf(columnMapping(destinationName("COLUMN_1")), decimalMapping(precision(10), scale(0)))),
                hasEntry(equalTo("2"), allOf(columnMapping(destinationName("COLUMN_2")), doubleMapping()))));
    }

    private void assertColumnTypeDetected(final List<String> csvLines, final boolean expectHeader,
            final Matcher<MappingDefinition> matcher) {
        final InferredMappingDefinition mappingDefinition = fetchSchema(csvLines);
        final Fields fields = (Fields) mappingDefinition.getMapping();
        final String expectedAdditionalConfig = "{\"csv-headers\": " + expectHeader + "}";
        final String expectedDestinationColumnName = expectHeader
                ? ToUpperSnakeCaseConverter.toUpperSnakeCase(csvLines.get(0))
                : "COLUMN_0";
        final String expectedFieldName = expectHeader ? csvLines.get(0) : "0";
        assertAll(
                () -> assertThat("has additional config", mappingDefinition.getAdditionalConfiguration().isPresent(),
                        is(true)),
                () -> assertThat("additional config", mappingDefinition.getAdditionalConfiguration().get(),
                        equalTo(expectedAdditionalConfig)),
                () -> assertThat(fields.getFieldsMap(), allOf( //
                        aMapWithSize(1), //
                        hasEntry(equalTo(expectedFieldName),
                                allOf(columnMapping(destinationName(expectedDestinationColumnName)), matcher)))));
    }

    Fields fetchSchema(final String... csvLines) {
        return (Fields) fetchSchema(asList(csvLines)).getMapping();
    }

    InferredMappingDefinition fetchSchema(final List<String> csvLines) {
        return fetchSchema(csvLines.stream().collect(joining("\n")));
    }

    InferredMappingDefinition fetchSchema(final String csvContent) {
        return new CsvSchemaFetcher()
                .fetchSchema(new RemoteFile("resourceName", 0, new StringRemoteFileContent(csvContent)));
    }
}
