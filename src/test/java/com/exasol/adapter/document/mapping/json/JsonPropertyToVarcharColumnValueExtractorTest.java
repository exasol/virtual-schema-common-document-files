package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo("test"));
    }

    @Test
    void testExtractFromNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNumberNode(Json.createValue(12.23));
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo("12.23"));
    }

    @Test
    void testExtractFromNull() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNullNode();
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo(null));
    }

    @Test
    void testExtractFromTrue() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(true);
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo("true"));
    }

    @Test
    void testExtractFromFalse() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonBooleanNode(false);
        assertThat(EXTRACTOR.mapStringValue(testNode), equalTo("false"));
    }

    @Test
    void testExtractFromObjectThrowsException() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonObjectNode(Json.createObjectBuilder().build());
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> EXTRACTOR.mapStringValue(testNode));
        assertThat(exception.getMessage(), equalTo("The JSON type Object can not be converted to string."));
    }

    @Test
    void testExtractFromArrayThrowsException() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonArrayNode(Json.createArrayBuilder().build());
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> EXTRACTOR.mapStringValue(testNode));
        assertThat(exception.getMessage(), equalTo("The JSON type Array can not be converted to string."));
    }
}