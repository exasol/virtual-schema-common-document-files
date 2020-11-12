package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.querypredicate.AbstractComparisonPredicate.Operator.EQUAL;
import static com.exasol.adapter.document.querypredicate.AbstractComparisonPredicate.Operator.LIKE;

import java.util.Set;

import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.StringFilterFactory;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.queryplanning.selectionextractor.SelectionExtractor;
import com.exasol.adapter.document.querypredicate.AbstractComparisonPredicate;
import com.exasol.adapter.document.querypredicate.ColumnLiteralComparisonPredicate;
import com.exasol.adapter.document.querypredicate.ComparisonPredicate;
import com.exasol.adapter.document.querypredicate.QueryPredicate;
import com.exasol.adapter.document.querypredicate.normalizer.DnfAnd;
import com.exasol.adapter.document.querypredicate.normalizer.DnfComparison;
import com.exasol.adapter.document.querypredicate.normalizer.DnfOr;
import com.exasol.adapter.sql.SqlLiteralString;
import com.exasol.errorreporting.ExaError;

/**
 * This class applies the predicates on the SOURCE_REFERENCE to the source string and splits the rest of the selection
 * as post-selection. By that only the required files are read.
 * <p>
 * If the selection can not be extracted, the source string is not changed, and the whole selection is used as post
 * selection.
 * </p>
 */
public class FilesSelectionExtractor {
    public static final StringFilterFactory STRING_FILTER_FACTORY = new StringFilterFactory();
    private static final Set<AbstractComparisonPredicate.Operator> SUPPORTED_OPERATORS = Set.of(EQUAL, LIKE);
    private final WildcardExpression sourceExpression;

    /**
     * Create a new instance of {@link FilesSelectionExtractor}.
     *
     * @param sourceString source string pattern as GLOB
     */
    public FilesSelectionExtractor(final String sourceString) {
        this.sourceExpression = WildcardExpression.fromGlob(sourceString);
    }

    /**
     * Split the selection into a post-selection and a modified, more-selective sourceString.
     *
     * @param selection selection to split
     * @return {@link Result}
     */
    public Result splitSelection(final QueryPredicate selection) {
        try {
            final SelectionExtractor selectionExtractor = new SelectionExtractor(
                    comparison -> comparison instanceof ColumnLiteralComparisonPredicate
                            && comparison.getComparedColumns().stream()
                                    .anyMatch(column -> column instanceof SourceReferenceColumnMapping)
                            && SUPPORTED_OPERATORS.contains(comparison.getOperator()));
            final SelectionExtractor.Result selectionExtractionResult = selectionExtractor
                    .extractIndexColumnSelection(selection);
            final QueryPredicate postSelection = selectionExtractionResult.getRemainingSelection().asQueryPredicate();
            final DnfOr or = selectionExtractionResult.getSelectedSelection();
            return new Result(postSelection,
                    STRING_FILTER_FACTORY.and(extractStringValueFromDnfOr(or), this.sourceExpression));
        } catch (final UnsupportedOperationException exception) {// todo refactor to a more specific exception
            return new Result(selection, this.sourceExpression);
        }
    }

    private StringFilter extractStringValueFromDnfOr(final DnfOr or) {
        return STRING_FILTER_FACTORY.or(or.getOperands().stream().map(this::extractStringValueFromDnfAnd));
    }

    private StringFilter extractStringValueFromDnfAnd(final DnfAnd dnfAnd) {
        return STRING_FILTER_FACTORY.and(dnfAnd.getOperands().stream().map(this::extractFilterFromDnfComparison));
    }

    private StringFilter extractFilterFromDnfComparison(final DnfComparison dnfComparison) {
        if (dnfComparison.isNegated()) {
            return STRING_FILTER_FACTORY.not(extractFilterFromComparison(dnfComparison.getComparisonPredicate()));
        } else {
            return extractFilterFromComparison(dnfComparison.getComparisonPredicate());
        }
    }

    private StringFilter extractFilterFromComparison(final ComparisonPredicate comparisonPredicate) {
        if (!(comparisonPredicate instanceof ColumnLiteralComparisonPredicate)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-6")
                    .message("Internal error. Please open a ticket. Unsupported comparison predicate.").toString());
        } else {
            return extractFromColumnLiteralComparison(comparisonPredicate);
        }
    }

    private StringFilter extractFromColumnLiteralComparison(final ComparisonPredicate comparisonPredicate) {
        final ColumnLiteralComparisonPredicate comparison = (ColumnLiteralComparisonPredicate) comparisonPredicate;
        final SqlLiteralString literal = (SqlLiteralString) comparison.getLiteral();
        final String sourceStringFilter = literal.getValue();
        if (comparisonPredicate.getOperator().equals(EQUAL)) {
            return extractFromEqualComparison(sourceStringFilter);
        } else if (comparisonPredicate.getOperator().equals(LIKE)) {
            return extractFromLikeExpression(sourceStringFilter);
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-5")
                    .message("Internal error. Please open a ticket. Unsupported operator {{OPERATOR}}.")
                    .parameter("OPERATOR", comparisonPredicate).toString());
        }
    }

    private StringFilter extractFromEqualComparison(final String sourceStringFilter) {
        return WildcardExpression.forNonWildcardString(sourceStringFilter);
    }

    private StringFilter extractFromLikeExpression(final String sourceStringFilter) {
        return WildcardExpression.fromLike(sourceStringFilter, '\\');// TODO remove hardcoded escape char
    }

    /**
     * Result of {@link #splitSelection(QueryPredicate)}.
     */
    public static class Result {
        private final QueryPredicate postSelection;
        private final StringFilter sourceFilter;

        private Result(final QueryPredicate postSelection, final StringFilter sourceFilter) {
            this.postSelection = postSelection;
            this.sourceFilter = sourceFilter;
        }

        /**
         * Get the post-selection.
         *
         * @return post-selection
         */
        public QueryPredicate getPostSelection() {
            return this.postSelection;
        }

        /**
         * Get the built filter for the source files.
         *
         * @return more selective source string
         */
        public StringFilter getSourceFilter() {
            return this.sourceFilter;
        }
    }
}
