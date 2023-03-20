package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.edml.*;

class LogicalTypeConverterTest {

    static Stream<Arguments> getUnsupportedTypes() {
        return Stream.of(//
                Arguments.of(Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.bsonType()).named("bson")), //
                Arguments.of(Types.primitive(FIXED_LEN_BYTE_ARRAY, REQUIRED).length(16)
                        .as(LogicalTypeAnnotation.uuidType()).named("id")), //
                Arguments.of(Types.primitive(FIXED_LEN_BYTE_ARRAY, REQUIRED).length(12)
                        .as(LogicalTypeAnnotation.IntervalLogicalTypeAnnotation.getInstance()).named("interval")), //
                Arguments.of(Types.primitive(BINARY, REQUIRED).length(12).as(LogicalTypeAnnotation.enumType())
                        .named("my_enum")), //
                Arguments.of(Types.requiredGroup().as(LogicalTypeAnnotation.MapKeyValueTypeAnnotation.getInstance())
                        .named("my_enum"))//
        );
    }

    private static Stream<Arguments> testConvertToStringCases() {
        return Stream.of(//
                Arguments.of(LogicalTypeAnnotation.stringType()), //
                Arguments.of(LogicalTypeAnnotation.jsonType()) //
        );
    }

    @ParameterizedTest
    @MethodSource("getUnsupportedTypes")
    void testUnsupported(final Type unsupportedType) {
        final LogicalTypeConverter converter = new LogicalTypeConverter();
        final LogicalTypeAnnotation logicalTypeAnnotation = unsupportedType.getLogicalTypeAnnotation();
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> converter.convert(logicalTypeAnnotation, "test"));
        assertThat(exception.getMessage(), startsWith("E-VSDF-59: Unsupported parquet type"));
    }

    @Test
    void testConvertList() {
        final Type itemType = Types.primitive(INT32, REQUIRED).named("element");
        final GroupType listType = Types.list(REQUIRED).element(itemType).named("ids");
        assertConvertsToJsonMapping(listType);
    }

    @Test
    void testConvertMap() {
        final Type keyType = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.stringType()).named("key");
        final Type valueType = Types.primitive(INT32, REQUIRED).named("value");
        final GroupType mapType = Types.map(REQUIRED).key(keyType).value(valueType).named("scores");
        assertConvertsToJsonMapping(mapType);
    }

    @Test
    void testConvertEnum() {
        final Type keyType = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.stringType()).named("key");
        final Type valueType = Types.primitive(INT32, REQUIRED).named("value");
        final GroupType listType = Types.map(REQUIRED).key(keyType).value(valueType).named("scores");
        assertConvertsToJsonMapping(listType);
    }

    private void assertConvertsToJsonMapping(final GroupType listType) {
        final ToJsonMapping toJsonMapping = (ToJsonMapping) new LogicalTypeConverter()
                .convert(listType.getLogicalTypeAnnotation(), "test");
        assertAll(//
                () -> assertThat(toJsonMapping.getVarcharColumnSize(), equalTo(2_000_000)),
                () -> assertThat(toJsonMapping.getDestinationName(), equalTo("test"))//
        );
    }

    @ParameterizedTest
    @MethodSource("testConvertToStringCases")
    void testConvertToString(final LogicalTypeAnnotation annotation) {
        final ToVarcharMapping toVarcharMapping = (ToVarcharMapping) new LogicalTypeConverter().convert(annotation,
                "my_string");
        assertAll(//
                () -> assertThat(toVarcharMapping.getVarcharColumnSize(), equalTo(2_000_000)),
                () -> assertThat(toVarcharMapping.getDestinationName(), equalTo("my_string"))//
        );
    }

    @Test
    void testConvertLogicalTypeDecimal() {
        assertConvertsToToDecimalMapping(LogicalTypeAnnotation.decimalType(2, 8), 2, 8);
    }

    @Test
    void testConvertLogicalTypeInt() {
        assertConvertsToToDecimalMapping(LogicalTypeAnnotation.intType(8, true), 0, 3);
    }

    @Test
    void testConvertLogicalTypeDate() {
        final MappingDefinition result = new LogicalTypeConverter().convert(LogicalTypeAnnotation.dateType(),
                ",birthday");
        assertThat(result, instanceOf(ToDateMapping.class));
    }

    @Test
    void testConvertLogicalTypeTime() {
        assertConvertsToToDecimalMapping(LogicalTypeAnnotation.timeType(true, LogicalTypeAnnotation.TimeUnit.MILLIS), 0,
                20);
    }

    @Test
    void testConvertLogicalTypeTimestamp() {
        final ToTimestampMapping toTimestampMapping = (ToTimestampMapping) new LogicalTypeConverter().convert(
                LogicalTypeAnnotation.timestampType(true, LogicalTypeAnnotation.TimeUnit.MILLIS), "my_timestamp");
        assertThat(toTimestampMapping.isUseTimestampWithLocalTimezoneType(), equalTo(true));
    }

    private void assertConvertsToToDecimalMapping(final LogicalTypeAnnotation type, final int scale,
            final int precision) {
        final ToDecimalMapping toDecimalMapping = (ToDecimalMapping) new LogicalTypeConverter().convert(type,
                "my_decimal");
        assertAll(//
                () -> assertThat(toDecimalMapping.getDecimalScale(), equalTo(scale)),
                () -> assertThat(toDecimalMapping.getDecimalPrecision(), equalTo(precision)),
                () -> assertThat(toDecimalMapping.getDestinationName(), equalTo("my_decimal"))//
        );
    }

}
