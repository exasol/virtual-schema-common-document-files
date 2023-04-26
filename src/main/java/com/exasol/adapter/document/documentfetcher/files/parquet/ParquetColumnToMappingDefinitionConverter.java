package com.exasol.adapter.document.documentfetcher.files.parquet;

import static com.exasol.adapter.document.documentfetcher.files.ColumnSizeCalculator.*;

import org.apache.parquet.schema.*;

import com.exasol.adapter.document.documentfetcher.files.ToUpperSnakeCaseConverter;
import com.exasol.adapter.document.edml.*;

/**
 * This class creates {@link MappingDefinition}s for parquet columns.
 */
class ParquetColumnToMappingDefinitionConverter {

    private static String buildColumnName(final Type column) {
        return ToUpperSnakeCaseConverter.toUpperSnakeCase(column.getName());
    }

    /**
     * Create a mapping definition for a parquet column.
     *
     * @param column column to create definition for
     * @return generated mapping definition
     */
    MappingDefinition convert(final Type column) {
        final LogicalTypeAnnotation logicalTypeAnnotation = column.getLogicalTypeAnnotation();
        if (logicalTypeAnnotation != null) {
            return new LogicalTypeConverter().convert(logicalTypeAnnotation, buildColumnName(column));
        } else {
            final NonLogicalTypeConvertVisitor visitor = new NonLogicalTypeConvertVisitor();
            column.accept(visitor);
            return visitor.getResult();
        }
    }

    private static class NonLogicalTypeConvertVisitor implements TypeVisitor {
        private MappingDefinition result;

        @Override
        public void visit(final GroupType groupType) {
            groupType.getLogicalTypeAnnotation();
            final Fields.FieldsBuilder fieldsBuilder = Fields.builder();
            for (final Type column : groupType.getFields()) {
                final MappingDefinition columnMapping = new ParquetColumnToMappingDefinitionConverter().convert(column);
                fieldsBuilder.mapField(column.getName(), columnMapping);
            }
            this.result = fieldsBuilder.build();
        }

        @Override
        public void visit(final MessageType messageType) {
            // message type is just a special group type
            this.visit((GroupType) messageType);
        }

        @Override
        public void visit(final PrimitiveType primitiveType) {
            this.result = buildMappingForPrimitiveType(primitiveType);
        }

        private MappingDefinition buildMappingForPrimitiveType(final PrimitiveType primitiveType) {
            final PrimitiveType.PrimitiveTypeName primitiveTypeName = primitiveType.getPrimitiveTypeName();
            switch (primitiveTypeName) {
            case INT32:
                return buildToDecimalMapping(primitiveType, INT_32_DIGITS);
            case INT64:
                return buildToDecimalMapping(primitiveType, INT_64_DIGITS);
            case INT96:
                return ToTimestampMapping.builder().destinationName(buildColumnName(primitiveType)).build();
            case BOOLEAN:
                return ToBoolMapping.builder().destinationName(buildColumnName(primitiveType)).build();
            case BINARY:
                return ToVarcharMapping.builder().destinationName(buildColumnName(primitiveType))
                        .varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE).build();
            case FIXED_LEN_BYTE_ARRAY:
                return ToVarcharMapping.builder().destinationName(buildColumnName(primitiveType))
                        .varcharColumnSize(primitiveType.getTypeLength()).build();
            case FLOAT:
            case DOUBLE:
                return ToDoubleMapping.builder().destinationName(buildColumnName(primitiveType)).build();
            default:
                throw UnsupportedTypeExceptionBuilder.createUnsupportedTypeException(
                        primitiveTypeName + "-primitive-type", buildColumnName(primitiveType));
            }
        }

        private ToDecimalMapping buildToDecimalMapping(final PrimitiveType primitiveType, final int precision) {
            return ToDecimalMapping.builder().decimalPrecision(precision).decimalScale(0)
                    .destinationName(buildColumnName(primitiveType)).build();
        }

        private MappingDefinition getResult() {
            return this.result;
        }
    }
}
