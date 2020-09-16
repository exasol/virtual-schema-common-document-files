package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.mapping.ColumnValueExtractor;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;

class JsonPropertyToColumnValueExtractorFactoryTest {

    public static final JsonPropertyToColumnValueExtractorFactory FACTORY = new JsonPropertyToColumnValueExtractorFactory();

    @Test
    void testBuildToVarcharExtractor() {
        final PropertyToVarcharColumnMapping mapping = PropertyToVarcharColumnMapping.builder().build();
        final ColumnValueExtractor<JsonNodeVisitor> result = FACTORY.getValueExtractorForColumn(mapping);
        assertThat(result, instanceOf(JsonPropertyToVarcharColumnValueExtractor.class));
    }
}