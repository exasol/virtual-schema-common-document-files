package com.exasol.adapter.document.documentnode.csv.converter;

import static com.exasol.adapter.document.documentnode.util.DocumentNodeMatchers.stringNode;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentpath.DocumentPathExpression;
import com.exasol.adapter.document.mapping.*;

class CsvValueTypeConverterRegistryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void creatingWithNullColumnListFails() {
        assertThrows(NullPointerException.class, () -> CsvValueTypeConverterRegistry.create(null));
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
        final PropertyToVarcharColumnMapping mappingWithoutPath = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(null).build();
        assertThrows(NullPointerException.class, () -> testee(mappingWithoutPath));
    }

    @Test
    void creatingWithEmptyPathFails() {
        final PropertyToVarcharColumnMapping mappingWithoutPath = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(DocumentPathExpression.builder().build()).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee(mappingWithoutPath));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-63: Path to source '/' has 0 segments. Please specify a path with exactly one segment."));
    }

    @Test
    void creatingWithTooLongPathFails() {
        final PropertyToVarcharColumnMapping mappingWithoutPath = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(
                        DocumentPathExpression.builder().addObjectLookup("dir1").addObjectLookup("dir2").build())
                .build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee(mappingWithoutPath));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-63: Path to source '/dir1/dir2' has 2 segments. Please specify a path with exactly one segment."));
    }

    @Test
    void creatingWithUnsupportedSegmentPathFails() {
        final PropertyToVarcharColumnMapping mappingWithoutPath = PropertyToVarcharColumnMapping.builder()
                .pathToSourceProperty(DocumentPathExpression.builder().addArrayLookup(0).build()).build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> testee(mappingWithoutPath));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-64: Segment 'com.exasol.adapter.document.documentpath.ArrayLookupPathSegment@0' of path '[0]' has an unsupported type 'com.exasol.adapter.document.documentpath.ArrayLookupPathSegment'. Please use only supported path segment of type 'com.exasol.adapter.document.documentpath.ObjectLookupPathSegment'."));
    }

    @Test
    void varcharMapping() {
        final CsvValueTypeConverterRegistry testee = testee(varcharMapping("col"));
        final DocumentNode result = testee.findConverter("col").convert("val");
        assertThat(result, stringNode("val"));
    }

    private PropertyToVarcharColumnMapping varcharMapping(final String columnName) {
        return PropertyToVarcharColumnMapping.builder() //
                .pathToSourceProperty(pathExpression(columnName)).build();
    }

    private DocumentPathExpression pathExpression(final String segment) {
        return DocumentPathExpression.builder().addObjectLookup(segment).build();
    }

    private CsvValueTypeConverterRegistry testee(final ColumnMapping... columnMappings) {
        return CsvValueTypeConverterRegistry.create(asList(columnMappings));
    }
}
