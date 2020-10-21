package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.querypredicate.*;
import com.exasol.adapter.sql.SqlLiteralString;

class FilesSelectionExtractorTest {
    private static final String TEST_FILE_JSON = "testFile-*.json";
    private static final FilesSelectionExtractor EXTRACTOR = new FilesSelectionExtractor(TEST_FILE_JSON);
    private static final SourceReferenceColumnMapping COLUMN = new SourceReferenceColumnMapping();

    static Stream<Arguments> getTestCases() {
        return Stream.of(//
                Arguments.of(getEqualCompare("testFile-1.json"), "testFile-1.json"),
                Arguments.of(new NotPredicate(getEqualCompare("testFile-1.json")), "NOT(testFile-1.json)"),
                Arguments.of(
                        new LogicalOperator(
                                Set.of(getEqualCompare("testFile-1.json"), getEqualCompare("testFile-2.json")),
                                LogicalOperator.Operator.AND),
                        "(testFile-\\E[12]\\Q.json) AND (testFile-\\E[12]\\Q.json)"),
                Arguments.of(
                        new LogicalOperator(
                                Set.of(getEqualCompare("testFile-1.json"), getEqualCompare("testFile-2.json")),
                                LogicalOperator.Operator.OR),
                        "(testFile-\\E[12]\\Q.json) OR (testFile-\\E[12]\\Q.json)")//
        );
    }

    private static ColumnLiteralComparisonPredicate getEqualCompare(final String literal) {
        return new ColumnLiteralComparisonPredicate(AbstractComparisonPredicate.Operator.EQUAL, COLUMN,
                new SqlLiteralString(literal));
    }

    @MethodSource("getTestCases")
    @ParameterizedTest
    void testExtractFromNonNestedPredicate(final QueryPredicate predicate, final String expectedResult) {
        final FilesSelectionExtractor.Result result = EXTRACTOR.splitSelection(predicate);
        assertThat(result.getSourceFilter().toString(), matchesPattern(
                "\\Q(" + expectedResult + ") AND (testFile-<DirectoryLimitedMultiCharWildcard>.json)\\E"));
    }

    @Test
    void testExtractFromLike() {
        final ColumnLiteralComparisonPredicate predicate = new ColumnLiteralComparisonPredicate(
                AbstractComparisonPredicate.Operator.LIKE, COLUMN, new SqlLiteralString("test%"));
        final FilesSelectionExtractor.Result result = EXTRACTOR.splitSelection(predicate);
        assertThat(result.getSourceFilter().toString(), equalTo(
                "(test<CrossDirectoryMultiCharWildcard>) AND (testFile-<DirectoryLimitedMultiCharWildcard>.json)"));
    }
}
