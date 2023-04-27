package com.exasol.adapter.document.documentfetcher.files.csv;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.StringRemoteFileContent;

import nl.jqno.equalsverifier.EqualsVerifier;

class CsvHeaderDetectorTest {

    @Test
    void emptyFileFailsParsing() {
        final List<String> emptyCsv = emptyList();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> assertHeader(false, emptyCsv));
        assertThat(exception.getMessage(), equalTo("E-VSDF-70: Failed to read CSV file 'resourceName'"));
        assertThat(exception.getCause().getMessage(),
                equalTo("Can't proceed because input file is empty and client has not specified headers"));
    }

    @Test
    void singleStringHasNoHeader() {
        assertHeader(false, List.of("valueOrHeader?"));
    }

    @ParameterizedTest
    @CsvSource({ "1", "1.1", "true", "FALSE", //
    })
    void singleColumnHasHeader(final String value) {
        assertHeader(true, List.of("header", value));
    }

    @ParameterizedTest
    @CsvSource({
            // Timestamp formats
            "2023-04-25 10:25:42", "2023-04-25 10:25:42.1234", "2023-04-25T10:25:42Z", "2023-04-25T10:25:42.1234Z",
            "2023-04-25 10:25:42Z", "2023-04-25 10:25:42.1234Z", "25.04.2023 10:25:42", "2023-04-25T10:25:42+001",
            "2023-04-25T10:25:42 Europe/Berlin", "2007-12-03T10:15:30+01:00[Europe/Paris]",
            // Date formats
            "2023-04-25", "23-04-25", "25.4.2023", })
    void singleColumnHasNoHeader(final String value) {
        assertHeader(false, List.of("header", value));
    }

    @Test
    void firstRowNonStringHeader() {
        assertHeader(false, List.of("v01,1,v03", "v11,v12,v13"));
    }

    @Test
    void mulitColumnsAllStringHasNoHeader() {
        assertHeader(false, List.of("h1,h2,h3", "v01,v02,v03", "v11,v12,v13"));
    }

    @Test
    void mulitColumnsNotAllStringHasHeader() {
        assertHeader(true, List.of("h1,h2,h3", "v01,2,v03", "v11,12,v13"));
    }

    @Test
    void mulitColumnsNotAllStringHasNoHeader() {
        assertHeader(false, List.of("v01,2,v03", "v11,12,v13"));
    }

    @Test
    void columnDataTypesEqualsContract() {
        EqualsVerifier.forClass(CsvHeaderDetector.ColumnDataTypes.class).verify();
    }

    private void assertHeader(final boolean expectHeader, final List<String> csvLines) {
        final String csvContent = csvLines.stream().collect(joining("\n"));
        final CsvHeaderDetector detector = new CsvHeaderDetector(
                new RemoteFile("resourceName", csvContent.length(), new StringRemoteFileContent(csvContent)));
        assertThat("has header", detector.hasHeaderRow(), equalTo(expectHeader));
    }
}
