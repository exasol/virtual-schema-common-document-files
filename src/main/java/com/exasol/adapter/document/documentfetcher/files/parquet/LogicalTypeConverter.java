package com.exasol.adapter.document.documentfetcher.files.parquet;

import static com.exasol.adapter.document.documentfetcher.files.ColumnSizeCalculator.*;
import static com.exasol.adapter.document.documentfetcher.files.parquet.UnsupportedTypeExceptionBuilder.createUnsupportedTypeException;

import java.util.Optional;

import org.apache.parquet.schema.LogicalTypeAnnotation;

import com.exasol.adapter.document.edml.*;

/**
 * This class converts parquet logical types to a {@link MappingDefinition EDML definitions}. See:
 * https://github.com/apache/parquet-format/blob/master/LogicalTypes.md.
 */
class LogicalTypeConverter {

    /**
     * Create a {@link MappingDefinition} for a parquet logical type.
     *
     * @param logicalType parquet type
     * @param columnName  column name
     * @return built {@link MappingDefinition}
     */
    MappingDefinition convert(final LogicalTypeAnnotation logicalType, final String columnName) {
        final ConvertVisitor visitor = new ConvertVisitor(columnName);
        logicalType.accept(visitor);
        return visitor.getResult();
    }

    private static class ConvertVisitor implements LogicalTypeAnnotation.LogicalTypeAnnotationVisitor<Void> {
        private final String columnName;
        private MappingDefinition result;

        private ConvertVisitor(final String columnName) {
            this.columnName = columnName;
        }

        private MappingDefinition getResult() {
            return this.result;
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.DecimalLogicalTypeAnnotation decimalLogicalType) {
            this.result = ToDecimalMapping.builder().decimalPrecision(decimalLogicalType.getPrecision())
                    .decimalScale(decimalLogicalType.getScale()).destinationName(this.columnName).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.StringLogicalTypeAnnotation stringLogicalType) {
            this.result = ToVarcharMapping.builder().destinationName(this.columnName)
                    .varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.MapLogicalTypeAnnotation mapLogicalType) {
            this.result = getToJsonMapping();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.ListLogicalTypeAnnotation listLogicalType) {
            this.result = getToJsonMapping();
            return Optional.empty();
        }

        private ToJsonMapping getToJsonMapping() {
            return ToJsonMapping.builder().destinationName(this.columnName).varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE)
                    .build();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.EnumLogicalTypeAnnotation enumLogicalType) {
            throw createUnsupportedTypeException("enum-logical-type", this.columnName);
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.DateLogicalTypeAnnotation dateLogicalType) {
            this.result = ToDateMapping.builder().destinationName(this.columnName).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.TimeLogicalTypeAnnotation timeLogicalType) {
            this.result = ToDecimalMapping.builder().decimalPrecision(INT_64_DIGITS).decimalScale(0)
                    .destinationName(this.columnName).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.TimestampLogicalTypeAnnotation timestampLogicalType) {
            this.result = ToTimestampMapping.builder().destinationName(this.columnName).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.IntLogicalTypeAnnotation intLogicalType) {
            this.result = ToDecimalMapping.builder()
                    .decimalPrecision(getNumberOfDigitsForInt(intLogicalType.getBitWidth())).decimalScale(0)
                    .destinationName(this.columnName).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.JsonLogicalTypeAnnotation jsonLogicalType) {
            this.result = ToVarcharMapping.builder().destinationName(this.columnName)
                    .varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE).build();
            return Optional.empty();
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.BsonLogicalTypeAnnotation bsonLogicalType) {
            throw createUnsupportedTypeException("bson-logical-type", this.columnName);
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.UUIDLogicalTypeAnnotation uuidLogicalType) {
            throw createUnsupportedTypeException("uuid-logical-type", this.columnName);
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.IntervalLogicalTypeAnnotation intervalLogicalType) {
            throw createUnsupportedTypeException("interval-logical-type", this.columnName);
        }

        @Override
        public Optional<Void> visit(final LogicalTypeAnnotation.MapKeyValueTypeAnnotation mapKeyValueLogicalType) {
            throw createUnsupportedTypeException("map-key-value-logical-type", this.columnName);
        }
    }
}
