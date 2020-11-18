package com.exasol.adapter.document.mapping.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.math.BigDecimal;
import java.util.stream.Stream;

import javax.json.Json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.*;
import com.exasol.adapter.document.mapping.PropertyToDecimalColumnMapping;
import com.exasol.adapter.document.mapping.PropertyToDecimalColumnValueExtractor;

class JsonPropertyToDecimalColumnValueExtractorTest {
    private static final PropertyToDecimalColumnMapping MAPPING = PropertyToDecimalColumnMapping.builder().build();
    private static final JsonPropertyToDecimalColumnValueExtractor EXTRACTOR = new JsonPropertyToDecimalColumnValueExtractor(
            MAPPING);

    private static Stream<Arguments> expectedNonNumberMapping() {
        return Stream.of(//
                Arguments.of(new JsonStringNode(Json.createValue("test")), "test"),
                Arguments.of(new JsonObjectNode(Json.createObjectBuilder().build()), "<object>"),
                Arguments.of(new JsonArrayNode(Json.createArrayBuilder().build()), "<array>"),
                Arguments.of(new JsonBooleanNode(false), "<false>"), Arguments.of(new JsonBooleanNode(true), "<true>"),
                Arguments.of(new JsonNullNode(), "<null>")//
        );
    }

    @Test
    void testExtractFromNumber() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonNumberNode(Json.createValue(12.23));
        final PropertyToDecimalColumnValueExtractor.ConvertedResult result = (PropertyToDecimalColumnValueExtractor.ConvertedResult) EXTRACTOR
                .mapValueToDecimal(testNode);
        assertThat(result.getResult(), equalTo(BigDecimal.valueOf(12.23)));
    }

    @MethodSource("expectedNonNumberMapping")
    @ParameterizedTest
    void testExtractFromNonNumbers(final DocumentNode<JsonNodeVisitor> testNode, final String expectedValue) {
        final PropertyToDecimalColumnValueExtractor.NotNumericResult result = (PropertyToDecimalColumnValueExtractor.NotNumericResult) EXTRACTOR
                .mapValueToDecimal(testNode);
        assertThat(result.getValue(), equalTo(expectedValue));
    }

    @Test
    void testExtractFrom() {
        final DocumentNode<JsonNodeVisitor> testNode = new JsonObjectNode(Json.createObjectBuilder().build());
        assertThat(EXTRACTOR.mapValueToDecimal(testNode),
                instanceOf(PropertyToDecimalColumnValueExtractor.NotNumericResult.class));
    }
}