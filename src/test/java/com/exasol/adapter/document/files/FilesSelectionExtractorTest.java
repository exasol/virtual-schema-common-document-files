package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.mapping.PropertyToVarcharColumnMapping;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.querypredicate.AbstractComparisonPredicate;
import com.exasol.adapter.document.querypredicate.ColumnLiteralComparisonPredicate;
import com.exasol.adapter.document.querypredicate.LogicalOperator;
import com.exasol.adapter.document.querypredicate.NoPredicate;
import com.exasol.adapter.sql.SqlLiteralString;

class FilesSelectionExtractorTest {
    private static final String TEST_FILE_JSON = "testFile-*.json";
    public static final FilesSelectionExtractor EXTRACTOR = new FilesSelectionExtractor(TEST_FILE_JSON);

    @CsvSource({ //
            "testFile-1.json, testFile-1.json", //
            "testFile-1abc.json, testFile-1abc.json", //
            "other.json, testFile-*.json"//
    })
    @ParameterizedTest
    void test(final String selection, final String expectedSourceString) {
        final FilesSelectionExtractor.Result result = EXTRACTOR
                .splitSelection(new ColumnLiteralComparisonPredicate(AbstractComparisonPredicate.Operator.EQUAL,
                        new SourceReferenceColumnMapping(), new SqlLiteralString(selection)));
        assertThat(result.getSourceString(), equalTo(expectedSourceString));
    }

    @Test
    void testEmptySelection() {
        final FilesSelectionExtractor.Result result = EXTRACTOR.splitSelection(new NoPredicate());
        assertThat(result.getSourceString(), equalTo(TEST_FILE_JSON));
    }

    @Test
    void testExtractFromAnd() {
        final ColumnLiteralComparisonPredicate comparison = new ColumnLiteralComparisonPredicate(
                AbstractComparisonPredicate.Operator.EQUAL, new SourceReferenceColumnMapping(),
                new SqlLiteralString("testFile-1.json"));
        final ColumnLiteralComparisonPredicate otherComparison = new ColumnLiteralComparisonPredicate(
                AbstractComparisonPredicate.Operator.EQUAL,
                PropertyToVarcharColumnMapping.builder().exasolColumnName("OTHER").build(),
                new SqlLiteralString("some value"));
        final LogicalOperator and = new LogicalOperator(Set.of(comparison, otherComparison),
                LogicalOperator.Operator.AND);
        final FilesSelectionExtractor.Result result = EXTRACTOR.splitSelection(and);
        assertAll(//
                () -> assertThat(result.getPostSelection(), equalTo(otherComparison)),
                () -> assertThat(result.getSourceString(), equalTo("testFile-1.json"))//
        );
    }
}
