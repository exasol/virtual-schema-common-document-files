package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.json.Json;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;

class JsonPropertyToVarcharColumnValueExtractorTest {
    private static final PropertyToVarcharColumnMapping MAPPING = PropertyToVarcharColumnMapping.builder().build();
    private static final JsonPropertyToVarcharColumnValueExtractor EXTRACTOR = new JsonPropertyToVarcharColumnValueExtractor(
            MAPPING);

    @Test
    void testExtractFromString() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonStringNode(Json.createValue("test"));
        final var result = EXTRACTOR.mapStringValue(testNode);
        assertAll(//
                () -> assertThat(result.isConverted(), equalTo(false)),
                () -> assertThat(result.getValue(), equalTo("test"))//
        );
    }

    @Test
    void testExtractFromNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNumberNode(Json.createValue(12.23));
        final var result = EXTRACTOR.mapStringValue(testNode);
        assertAll(//
                () -> assertThat(result.isConverted(), equalTo(true)),
                () -> assertThat(result.getValue(), equalTo("12.23"))//
        );
    }

    @Test
    void testExtractFromNull() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNullNode();
        final var result = EXTRACTOR.mapStringValue(testNode);
        assertAll(//
                () -> assertThat(result.isConverted(), equalTo(false)),
                () -> assertThat(result.getValue(), equalTo(null))//
        );
    }

    @Test
    void testExtractFromTrue() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(true);
        final var result = EXTRACTOR.mapStringValue(testNode);
        assertAll(//
                () -> assertThat(result.isConverted(), equalTo(true)),
                () -> assertThat(result.getValue(), equalTo("true"))//
        );
    }

    @Test
    void testExtractFromFalse() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(false);
        final var result = EXTRACTOR.mapStringValue(testNode);
        assertAll(//
                () -> assertThat(result.isConverted(), equalTo(true)),
                () -> assertThat(result.getValue(), equalTo("false"))//
        );
    }

    @Test
    void testExtractFromObjectThrowsException() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonObjectNode(Json.createObjectBuilder().build());
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo(null));
    }

    @Test
    void testExtractFromArrayThrowsException() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonArrayNode(Json.createArrayBuilder().build());
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo(null));
    }
}