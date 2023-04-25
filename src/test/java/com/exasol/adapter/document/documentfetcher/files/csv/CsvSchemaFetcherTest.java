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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.StringRemoteFileContent;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;

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
        assertColumnTypeDetected(List.of("val0", "val1", "val2"), varcharMapping(varcharColumnsSize(254)));
    }

    @Test
    void testCharColumn() {
        assertColumnTypeDetected(List.of("a", "b", "c"), varcharMapping(varcharColumnsSize(1)));
    }

    @ParameterizedTest
    @CsvSource({ "true", "false", "TRUE", "FALSE", "True", "False" })
    void testBooleanColumn(final String value) {
        assertColumnTypeDetected(List.of(value), boolMapping());
    }

    @ParameterizedTest
    @CsvSource({ "1", "-1", "0", //
            "2147483647", // max int
            "-2147483648" // min int
    })
    void testIntColumn(final String value) {
        assertColumnTypeDetected(List.of(value), decimalMapping(precision(10), scale(0)));
    }

    @ParameterizedTest
    @CsvSource({ "2147483648", // max int + 1
            "-2147483649", // min int - 1
            "9223372036854775807", // max long
            "-9223372036854775808" // min long
    })
    void testLongColumn(final String value) {
        assertColumnTypeDetected(List.of(value), decimalMapping(precision(19), scale(0)));
    }

    @ParameterizedTest
    @CsvSource({ "1.2", "0.1", "0.0", "1.2e7", "1.2e-7", ".3", ".4e7", ".6e-9" })
    void testDoubleColumn(final String value) {
        assertColumnTypeDetected(List.of(value), decimalMapping(precision(36), scale(10)));
    }

    @Disabled("Date is currently not supported")
    @ParameterizedTest
    @CsvSource({ "2023-04-25", "20230425" })
    void testDateColumn(final String value) {
        assertColumnTypeDetected(List.of(value), dateMapping());
    }

    @ParameterizedTest
    @CsvSource({ "2023-04-25", "23-04-25", "25.4.2023", })
    void testUnsupportedDateColumnConvertedToVarchar(final String value) {
        assertColumnTypeDetected(List.of(value), varcharMapping());
    }

    @ParameterizedTest
    @CsvSource({ "2023-04-25 10:25:42", "2023-04-25 10:25:42.1234", "2023-04-25T10:25:42Z", "2023-04-25T10:25:42.1234Z",
            "2023-04-25 10:25:42Z", "2023-04-25 10:25:42.1234Z", })
    void testTimestampColumn(final String value) {
        assertColumnTypeDetected(List.of(value), timestampMapping());
    }

    @ParameterizedTest
    @CsvSource({ "25.04.2023 10:25:42", "2023-04-25T10:25:42+001", "2023-04-25T10:25:42 Europe/Berlin",
            "2007-12-03T10:15:30+01:00[Europe/Paris]", })
    void testUnsupportedTimestampColumnConvertedToVarchar(final String value) {
        assertColumnTypeDetected(List.of(value), varcharMapping());
    }

    @Test
    void testMultipleColumns() {
        final Fields fields = fetchSchema(List.of("val0,0,1.1", "val1,1,2.2", "val2,2,3.3"));
        assertAll(() -> assertThat(fields.getFieldsMap(), aMapWithSize(3)),
                () -> assertThat(fields.getFieldsMap().get("0"),
                        allOf(columnMapping(destinationName("COLUMN_0")), varcharMapping(varcharColumnsSize(254)))),
                () -> assertThat(fields.getFieldsMap().get("1"),
                        allOf(columnMapping(destinationName("COLUMN_1")), decimalMapping(precision(10), scale(0)))),
                () -> assertThat(fields.getFieldsMap().get("2"),
                        allOf(columnMapping(destinationName("COLUMN_2")), decimalMapping(precision(36), scale(10)))));
    }

    private void assertColumnTypeDetected(final List<String> csvLines, final Matcher<MappingDefinition> matcher) {
        final Fields fields = fetchSchema(csvLines);
        assertAll(() -> assertThat(fields.getFieldsMap(), aMapWithSize(1)),
                () -> assertThat(fields.getFieldsMap().get("0"), columnMapping(destinationName("COLUMN_0"))),
                () -> assertThat(fields.getFieldsMap().get("0"), matcher));
    }

    Fields fetchSchema(final String... csvLines) {
        return (Fields) fetchSchema(asList(csvLines));
    }

    Fields fetchSchema(final List<String> csvLines) {
        return (Fields) fetchSchema(csvLines.stream().collect(joining("\n")));
    }

    MappingDefinition fetchSchema(final String csvContent) {
        return new CsvSchemaFetcher()
                .fetchSchema(new RemoteFile("resourceName", 0, new StringRemoteFileContent(csvContent)));
    }
}
