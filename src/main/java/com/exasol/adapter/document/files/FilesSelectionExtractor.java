package com.exasol.adapter.document.files;

import java.util.Set;
import java.util.regex.Pattern;

import com.exasol.adapter.document.documentfetcher.files.GlobToRegexConverter;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.queryplanning.selectionextractor.SelectionExtractor;
import com.exasol.adapter.document.querypredicate.*;
import com.exasol.adapter.document.querypredicate.normalizer.DnfAnd;
import com.exasol.adapter.document.querypredicate.normalizer.DnfComparison;
import com.exasol.adapter.document.querypredicate.normalizer.DnfOr;
import com.exasol.adapter.sql.SqlLiteralString;

/**
 * This class applies the predicates on the SOURCE_REFERENCE to the source string and splits the rest of the selection
 * as post-selection. By that only the required files are read.
 * <p>
 * If the selection can not be extracted, for example because it combines two conditions on SOURCE_REFERENCE using AND
 * or OR, the source string is not changed, and the whole selection is used as post selection.
 * </p>
 */
public class FilesSelectionExtractor {
    private final String sourceString;
    private final Pattern sourceStringPattern;

    /**
     * Create a new instance of {@link FilesSelectionExtractor}.
     * 
     * @param sourceString source string pattern as GLOB
     */
    public FilesSelectionExtractor(final String sourceString) {
        this.sourceString = sourceString;
        this.sourceStringPattern = GlobToRegexConverter.convert(sourceString);
    }

    /**
     * Split the selection into a post-selection and a modified, more-selective sourceString.
     * 
     * @param selection selection to split
     * @return {@link Result}
     */
    public Result splitSelection(final QueryPredicate selection) {
        final SelectionExtractor selectionExtractor = new SelectionExtractor(comparison -> comparison
                .getComparedColumns().stream().anyMatch(column -> column instanceof SourceReferenceColumnMapping));
        final SelectionExtractor.Result selectionExtractionResult = selectionExtractor
                .extractIndexColumnSelection(selection);
        final QueryPredicate postSelection = selectionExtractionResult.getRemainingSelection().asQueryPredicate();
        final DnfOr or = selectionExtractionResult.getSelectedSelection();
        try {
            return new Result(postSelection, extractStringValueFromDnfOr(or));
        } catch (final CanNotSplitException exception) {
            return new Result(selection, this.sourceString);
        } catch (final QueryIsEmptyException exception) {
            final QueryPredicate falsePredicate = new NotPredicate(new NoPredicate());
            return new Result(falsePredicate, this.sourceString);
        }
    }

    @SuppressWarnings("java:S3655") // findFirst is safe here since it is checked with if beforehand
    private String extractStringValueFromDnfOr(final DnfOr or) {
        final Set<DnfAnd> dnfAnds = or.getOperands();
        if (dnfAnds.size() != 1) {
            throw new CanNotSplitException();
        } else {
            return extractStringValueFromDnfAnd(dnfAnds.stream().findFirst().get());
        }
    }

    @SuppressWarnings("java:S3655") // findFirst is safe here since it is checked with if beforehand
    private String extractStringValueFromDnfAnd(final DnfAnd dnfAnd) {
        final Set<DnfComparison> operands = dnfAnd.getOperands();
        if (operands.size() != 1) {
            throw new CanNotSplitException();
        } else {
            return extractFilterFromDnfComparison(operands.stream().findFirst().get());
        }
    }

    private String extractFilterFromDnfComparison(final DnfComparison dnfComparison) {
        if (dnfComparison.isNegated()) {
            throw new CanNotSplitException();
        } else {
            return extractFilterFromComparison(dnfComparison.getComparisonPredicate());
        }
    }

    private String extractFilterFromComparison(final ComparisonPredicate comparisonPredicate) {
        if (!(comparisonPredicate instanceof ColumnLiteralComparisonPredicate)
                || !comparisonPredicate.getOperator().equals(AbstractComparisonPredicate.Operator.EQUAL)) {
            throw new CanNotSplitException();
        } else {
            final ColumnLiteralComparisonPredicate comparison = (ColumnLiteralComparisonPredicate) comparisonPredicate;
            final SqlLiteralString literal = (SqlLiteralString) comparison.getLiteral();
            final String sourceStringFilter = literal.getValue();
            if (this.sourceStringPattern.matcher(sourceStringFilter).matches()) {
                return sourceStringFilter;
            } else {
                throw new QueryIsEmptyException();
            }
        }
    }

    /**
     * Result of {@link #splitSelection(QueryPredicate)}.
     */
    public static class Result {
        private final QueryPredicate postSelection;
        private final String sourceString;

        private Result(final QueryPredicate postSelection, final String sourceString) {
            this.postSelection = postSelection;
            this.sourceString = sourceString;
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
         * Get the more selective source string.
         * 
         * @return more selective source string
         */
        public String getSourceString() {
            return this.sourceString;
        }
    }

    private static class CanNotSplitException extends RuntimeException {
    }

    private static class QueryIsEmptyException extends RuntimeException {
    }
}
