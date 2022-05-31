package com.exasol.adapter.document.documentnode.csv;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.BigDecimalHolderNode;
import com.exasol.adapter.document.documentnode.holder.BooleanHolderNode;
import com.exasol.adapter.document.documentnode.holder.NullHolderNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentnode.json.JsonArrayNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.documentnode.json.JsonObjectNode;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class CsvNodeFactoryTest {
//
//    static Stream<Arguments> simpleTypeConversionTestCases() {
//        return Stream.of(//
//                Arguments.of(Json.createValue("test"), StringHolderNode.class), //
//                Arguments.of(Json.createValue(123), BigDecimalHolderNode.class), //
//                Arguments.of(Json.createValue(1.23), BigDecimalHolderNode.class), //
//                Arguments.of(JsonValue.TRUE, BooleanHolderNode.class), //
//                Arguments.of(JsonValue.FALSE, BooleanHolderNode.class), //
//                Arguments.of(JsonValue.NULL, NullHolderNode.class), //
//                Arguments.of(Json.createArrayBuilder().add(1).build(), JsonArrayNode.class), //
//                Arguments.of(Json.createObjectBuilder().add("key", 1).build(), JsonObjectNode.class)//
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("simpleTypeConversionTestCases")
//    void testSimpleTypeConversion(final JsonValue jsonValue, final Class<?> expectedType) {
//        final DocumentNode result = JsonNodeFactory.getInstance().getJsonNode(jsonValue);
//        assertThat(result, instanceOf(expectedType));
//    }
}
