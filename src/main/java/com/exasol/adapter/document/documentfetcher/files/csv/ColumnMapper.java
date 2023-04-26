package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.documentfetcher.files.ColumnSizeCalculator.*;

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.AbstractToColumnMapping.AbstractToColumnMappingBuilder;
import com.exasol.errorreporting.ExaError;

import io.deephaven.csv.parsers.DataType;

class ColumnMapper {
    private final String source;
    private final String destination;
    private final DataType dataType;

    ColumnMapper(final String source, final String destination, final DataType dataType) {
        this.source = source;
        this.destination = destination;
        this.dataType = dataType;
    }

    String getSource() {
        return this.source;
    }

    String getDestination() {
        return this.destination;
    }

    MappingDefinition getMapping() {
        return createBuilder(this.dataType).destinationName(this.destination).build();
    }

    private AbstractToColumnMappingBuilder<?, ?> createBuilder(final DataType dataType) {
        switch (dataType) {
        case STRING:
            return ToVarcharMapping.builder().varcharColumnSize(MAX_VARCHAR_COLUMN_SIZE);
        case CHAR:
            return ToVarcharMapping.builder().varcharColumnSize(1);
        case BOOLEAN_AS_BYTE:
            return ToBoolMapping.builder();
        case BYTE:
        case SHORT:
        case INT:
            return ToDecimalMapping.builder().decimalPrecision(INT_32_DIGITS).decimalScale(0);
        case LONG:
            return ToDecimalMapping.builder().decimalPrecision(INT_64_DIGITS).decimalScale(0);
        case FLOAT:
        case DOUBLE:
            return ToDoubleMapping.builder();
        case DATETIME_AS_LONG:
        case TIMESTAMP_AS_LONG:
            // Not supported
            return ToVarcharMapping.builder();
        default:
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-71")
                    .message("Unknown data type {{data type}}.", dataType).ticketMitigation().toString());
        }
    }
}
