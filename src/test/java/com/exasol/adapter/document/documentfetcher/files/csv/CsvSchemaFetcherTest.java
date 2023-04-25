package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.testutil.MappingMatchers.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.StringRemoteFileContent;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;

class CsvSchemaFetcherTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testVarcharColumn() {
        assertColumnTypeDetected(List.of("val0", "val1", "val2"), varcharMapping(varcharColumnsSize(254)));
    }

    @Test
    void testCharColumn() {
        assertColumnTypeDetected(List.of("a", "b", "c"), varcharMapping(varcharColumnsSize(1)));
    }

    @Test
    void testBooleanColumn() {
        assertColumnTypeDetected(List.of("true", "false"), boolMapping());
    }

    @Test
    void testIntColumn() {
        assertColumnTypeDetected(List.of("1", String.valueOf(Integer.MAX_VALUE)),
                decimalMapping(precision(10), scale(0)));
    }

    @Test
    void testLongColumn() {
        assertColumnTypeDetected(List.of("1", String.valueOf(Long.MAX_VALUE)), decimalMapping(precision(19), scale(0)));
    }

    @Test
    void testDoubleColumn() {
        assertColumnTypeDetected(List.of("1.2"), decimalMapping(precision(36), scale(10)));
    }

    @Disabled("Date is currently not supported")
    @ParameterizedTest
    @CsvSource({ "2023-04-25", "20230425" })
    void testDateColumn(final String value) {
        assertColumnTypeDetected(List.of(value), dateMapping());
    }

    @ParameterizedTest
    @CsvSource({ "2023-04-25 10:25:42", "2023-04-25 10:25:42.1234", "2023-04-25T10:25:42Z", "2023-04-25T10:25:42.1234Z",
            "2023-04-25 10:25:42Z", "2023-04-25 10:25:42.1234Z",
    // unsupported:
    // "25.04.2023 10:25:42",
    })
    void testTimestampColumn(final String value) {
        assertColumnTypeDetected(List.of(value), timestampMapping());
    }

    @Test
    void testMixedTimestampColumn() {
        assertColumnTypeDetected(List.of("2023-04-25 10:25:42", "2023-04-25T10:25:42Z"), timestampMapping());
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
        return new CsvSchemaFetcher().fetchSchema(new RemoteFile("", 0, new StringRemoteFileContent(csvContent)));
    }
}
