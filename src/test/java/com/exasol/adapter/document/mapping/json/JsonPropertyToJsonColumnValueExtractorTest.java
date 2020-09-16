package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.json.Json;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToJsonColumnMapping;

class JsonPropertyToJsonColumnValueExtractorTest {
    private static final PropertyToJsonColumnMapping MAPPING = PropertyToJsonColumnMapping.builder().build();
    private static final JsonPropertyToJsonColumnValueExtractor EXTRACTOR = new JsonPropertyToJsonColumnValueExtractor(
            MAPPING);

    @Test
    void testExtractFromString() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonStringNode(Json.createValue("test"));
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("\"test\""));
    }

    @Test
    void testExtractFromNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNumberNode(Json.createValue(12.23));
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("12.23"));
    }

    @Test
    void testExtractFromNull() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNullNode();
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("null"));
    }

    @Test
    void testExtractFromTrue() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(true);
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("true"));
    }

    @Test
    void testExtractFromFalse() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(false);
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("false"));
    }

    @Test
    void testExtractFromObject() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonObjectNode(
                Json.createObjectBuilder().add("key", "value").build());
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("{\"key\":\"value\"}"));
    }

    @Test
    void testExtractFromArray() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonArrayNode(Json.createArrayBuilder().add(1).build());
        assertThat(EXTRACTOR.mapJsonValue(testNode), equalTo("[1]"));
    }

}