package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.*;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.mapping.auto.ColumnNameConverter;

class ParquetColumnToMappingDefinitionConverterTest {

    @ParameterizedTest
    @CsvSource({ //
            "INT32, 10", "INT64, 20" })
    void testConvertIntType(final PrimitiveType.PrimitiveTypeName type, final int expectedPrecision) {
        final PrimitiveType intType = Types.primitive(type, REQUIRED).named("id");
        assertConvertsToToDecimalMapping(intType, 0, expectedPrecision, "ID");
    }

    @Test
    void testConvertBooleanType() {
        final PrimitiveType boolType = Types.primitive(BOOLEAN, REQUIRED).named("isActive");
        final ToBoolMapping toBoolMapping = (ToBoolMapping) convert(boolType);
        assertThat(toBoolMapping.getDestinationName(), equalTo("IS_ACTIVE"));
    }

    @Test
    void testConvertBinaryType() {
        final PrimitiveType type = Types.primitive(BINARY, REQUIRED).named("myString");
        assertConvertsToToVarcharMapping(type, "MY_STRING", 2_000_000);
    }

    @Test
    void testConvertFixedLengthByteArrayType() {
        final PrimitiveType type = Types.primitive(FIXED_LEN_BYTE_ARRAY, REQUIRED).length(123).named("myString");
        assertConvertsToToVarcharMapping(type, "MY_STRING", 123);
    }

    @ParameterizedTest
    @CsvSource({ "FLOAT", "DOUBLE" })
    void testConvertFloatTypes(final PrimitiveType.PrimitiveTypeName floatingPointType) {
        final PrimitiveType boolType = Types.primitive(floatingPointType, REQUIRED).named("score");
        final ToDoubleMapping toDoubleMapping = (ToDoubleMapping) convert(boolType);
        assertThat(toDoubleMapping.getDestinationName(), equalTo("SCORE"));
    }

    private void assertConvertsToToDecimalMapping(final Type type, final int scale, final int precision,
            final String name) {
        final ToDecimalMapping toDecimalMapping = (ToDecimalMapping) convert(type);
        assertAll(//
                () -> assertThat(toDecimalMapping.getDecimalScale(), equalTo(scale)),
                () -> assertThat(toDecimalMapping.getDecimalPrecision(), equalTo(precision)),
                () -> assertThat(toDecimalMapping.getDestinationName(), equalTo(name))//
        );
    }

    @Test
    void testConvertInt96ToTimestamp() {
        final Type logicalTimestampType = Types.primitive(INT96, REQUIRED).named("myTimestamp");
        final ToTimestampMapping toTimestampMapping = (ToTimestampMapping) convert(logicalTimestampType);
        assertAll(//
                () -> assertThat(toTimestampMapping.getDestinationName(), equalTo("MY_TIMESTAMP")), //
                () -> assertThat(toTimestampMapping.getNotTimestampBehavior(),
                        equalTo(ConvertableMappingErrorBehaviour.ABORT))//
        );
    }

    private void assertConvertsToToVarcharMapping(final Type type, final String expectedName, final int expectedSize) {
        final ToVarcharMapping toVarcharMapping = (ToVarcharMapping) convert(type);
        assertAll(//
                () -> assertThat(toVarcharMapping.getVarcharColumnSize(), equalTo(expectedSize)),
                () -> assertThat(toVarcharMapping.getDestinationName(), equalTo(expectedName))//
        );
    }

    @Test
    void testConvertLogicalType() {
        final Type logicalStringType = Types.primitive(BINARY, REQUIRED).as(LogicalTypeAnnotation.stringType())
                .named("value");
        assertConvertsToToVarcharMapping(logicalStringType, "VALUE", 2_000_000);
    }

    @Test
    void testConvertNestedList() {
        final Type itemType = Types.primitive(INT32, REQUIRED).named("element");
        final GroupType listType = Types.list(REQUIRED).element(itemType).named("ids");
        final MessageType schema = new MessageType("test", listType);
        final Fields result = (Fields) convert(schema);
        assertThat(result.getFieldsMap().get("IDS"), instanceOf(ToJsonMapping.class));
    }

    private MappingDefinition convert(final Type column) {
        return new ParquetColumnToMappingDefinitionConverter(ColumnNameConverter.upperSnakeCaseConverter())
                .convert(column);
    }
}
