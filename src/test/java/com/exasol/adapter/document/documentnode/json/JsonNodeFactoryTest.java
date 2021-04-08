package com.exasol.adapter.document.documentnode.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.stream.Stream;

import javax.json.JsonValue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import com.exasol.adapter.document.documentnode.DocumentNode;

class JsonNodeFactoryTest {

    static Stream<Arguments> simpleTypeConversionTestCases() {
        return Stream.of();
    }

    @ParameterizedTest
    void testSimpleTypeConversion(final JsonValue jsonValue, final Class<?> expectedType) {
        final DocumentNode result = JsonNodeFactory.getInstance().getJsonNode(jsonValue);
        assertThat(result, instanceOf(expectedType));
    }
}