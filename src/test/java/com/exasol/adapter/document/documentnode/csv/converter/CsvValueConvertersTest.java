package com.exasol.adapter.document.documentnode.csv.converter;

import static com.exasol.adapter.document.testutil.DocumentNodeMatchers.*;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentpath.*;
import com.exasol.adapter.document.mapping.*;

class CsvValueConvertersTest {

    @Test
    void creatingWithNullColumnListFails() {
        assertThrows(NullPointerException.class, () -> CsvValueConverters.create(null));
    }

    @Test
    void creatingWithEmptyColumnList() {
        assertDoesNotThrow(() -> testee());
    }

    @Test
    void creatingWithoutNonPropertyMappingFails() {
        final ColumnMapping mapping = IterationIndexColumnMapping.builder().build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee(mapping));
        assertThat(exception.getMessage(), startsWith("E-VSDF-62: Column mapping of type '"
                + mapping.getClass().getName() + "' (value: " + mapping + ") is not supported."));
    }

    @Test
    void creatingWithoutPathFails() {
        final ColumnMapping mapping = PropertyToVarcharColumnMapping.builder().pathToSourceProperty(null).build();
        assertThrows(NullPointerException.class, () -> testee(mapping));
    }

    @Test
    void creatingWithEmptyPathFails() {
        final ColumnMapping mapping = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(DocumentPathExpression.builder().build()).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee(mapping));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-63: Path to source '/' has 0 segments. Please specify a path with exactly one segment."));
    }

    @Test
    void creatingWithTooLongPathFails() {
        final ColumnMapping mapping = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(
                        DocumentPathExpression.builder().addObjectLookup("dir1").addObjectLookup("dir2").build())
                .build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee(mapping));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-63: Path to source '/dir1/dir2' has 2 segments. Please specify a path with exactly one segment."));
    }

    @Test
    void creatingWithUnsupportedSegmentPathFails() {
        final PathSegment segment = new ArrayLookupPathSegment(0);
        final ColumnMapping mapping = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(DocumentPathExpression.builder().addPathSegment(segment).build()).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee(mapping));
        assertThat(exception.getMessage(),
                equalTo("E-VSDF-64: Segment '" + segment + "' of path '[0]' has an unsupported type '"
                        + segment.getClass().getName() + "'. Please use only supported path segment of type '"
                        + ObjectLookupPathSegment.class.getName() + "'."));
    }

    @Test
    void creatingWithUnsupportedMappingFails() {
        final ColumnMapping mapping = IterationIndexColumnMapping.builder().build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> testee(mapping));
        assertThat(exception.getMessage(), equalTo(

                "E-VSDF-62: Column mapping of type '" + mapping.getClass().getName() + "' (value: " + mapping
                        + ") is not supported. Please use only supported column mappings."));
    }

    @Test
    void varcharMapping() {
        final CsvValueConverters testee = testee(varcharMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("val");
        assertThat(result, stringNode("val"));
    }

    @Test
    void boolMapping() {
        final CsvValueConverters testee = testee(boolMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("True");
        assertThat(result, booleanNode(true));
    }

    @Test
    void decimalMapping() {
        final CsvValueConverters testee = testee(decimalMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("1.234");
        assertThat(result, decimalNode(1.234));
    }

    @Test
    void doubleMapping() {
        final CsvValueConverters testee = testee(doubleMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("1.234");
        assertThat(result, doubleNode(1.234));
    }

    @Test
    void dateMapping() {
        final CsvValueConverters testee = testee(dateMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("2023-04-21");
        assertThat(result, dateNode("2023-04-21"));
    }

    @Test
    void timestampMapping() {
        final CsvValueConverters testee = testee(timestampMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("2023-04-21 15:13:42");
        assertThat(result, timestampNode("2023-04-21 15:13:42"));
    }

    @Test
    void sourceReferenceIgnored() {
        final CsvValueConverters testee = testee(SourceReferenceColumnMapping.builder().build(), varcharMapping("col"));
        final DocumentNode result = testee.findConverter(0).convert("val");
        assertThat(result, stringNode("val"));
    }

    @Test
    void findConverterByColumnNameSucceeds() {
        final CsvValueConverters testee = testee(varcharMapping("col"));
        assertThat(testee.findConverter("col"), notNullValue());
    }

    @Test
    void findConverterByColumnIndexSucceeds() {
        final CsvValueConverters testee = testee(varcharMapping("col"));
        assertThat(testee.findConverter(0), notNullValue());
    }

    @Test
    void findConverterByColumnNameFails() {
        final CsvValueConverters testee = testee(varcharMapping("col"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> testee.findConverter("no-such-col"));
        assertThat(exception.getMessage(),
                startsWith("E-VSDF-61: No CSV value converter found for column 'no-such-col'. Available converters:"));
    }

    @Test
    void findConverterByColumnIndexFails() {
        final CsvValueConverters testee = testee(varcharMapping("col"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> testee.findConverter(1));
        assertThat(exception.getMessage(),
                startsWith("E-VSDF-60: No CSV value converter found for column 1. Available converters:"));
    }

    private ColumnMapping varcharMapping(final String columnName) {
        return PropertyToVarcharColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping boolMapping(final String columnName) {
        return PropertyToBoolColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping decimalMapping(final String columnName) {
        return PropertyToDecimalColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping doubleMapping(final String columnName) {
        return PropertyToDoubleColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping dateMapping(final String columnName) {
        return PropertyToDateColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private ColumnMapping timestampMapping(final String columnName) {
        return PropertyToTimestampColumnMapping.builder().pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addObjectLookup(segment).build();
    }

    private CsvValueConverters testee(final ColumnMapping... columnMappings) {
        return CsvValueConverters.create(asList(columnMappings));
    }
}
