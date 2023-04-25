package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.testutil.MappingMatchers.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.StringRemoteFileContent;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;

class CsvSchemaFetcherTest {

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testSingleVarcharColumnWithHeader() {
        final MappingDefinition schema = fetchSchema("colName1", "val1", "val2");
        final Fields fields = (Fields) schema;
        assertThat(fields.getFieldsMap().get("colName1"),
                allOf(columnMapping(destinationName("COL_NAME1")), varcharMapping(varcharColumnsSize(254))));
    }

    MappingDefinition fetchSchema(final String... csvLines) {
        return fetchSchema(asList(csvLines));
    }

    MappingDefinition fetchSchema(final List<String> csvLines) {
        return fetchSchema(csvLines.stream().collect(joining("\n")));
    }

    MappingDefinition fetchSchema(final String csvContent) {
        return new CsvSchemaFetcher().fetchSchema(new RemoteFile("", 0, new StringRemoteFileContent(csvContent)));
    }
}
