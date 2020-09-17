package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import javax.json.Json;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.documentnode.json.JsonNumberNode;
import com.exasol.adapter.document.documentnode.json.JsonStringNode;
import com.exasol.adapter.document.mapping.PropertyToDecimalColumnMapping;

class JsonPropertyToDecimalColumnValueExtractorTest {
    private static final PropertyToDecimalColumnMapping MAPPING = PropertyToDecimalColumnMapping.builder().build();
    private static final JsonPropertyToDecimalColumnValueExtractor EXTRACTOR = new JsonPropertyToDecimalColumnValueExtractor(
            MAPPING);

    @Test
    void testExtractFromNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNumberNode(Json.createValue(12.23));
        assertThat(EXTRACTOR.mapValueToDecimal(testNode), equalTo(BigDecimal.valueOf(12.23)));
    }

    @Test
    void testExtractFromNonNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonStringNode(Json.createValue("test"));
        assertThat(EXTRACTOR.mapValueToDecimal(testNode), equalTo(null));
    }
}