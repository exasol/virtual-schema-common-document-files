package com.exasol.adapter.document.files.stringfilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Factory for {@link StringFilter}.
 */
public class StringFilterFactory {

    /**
     * Get an AND expression.
     * 
     * @param operands operands for the AND
     * @return built AND
     */
    public StringFilter and(final StringFilter... operands) {
        return and(Arrays.stream(operands));
    }

    /**
     * Get an AND expression.
     *
     * @param operands operands for the AND
     * @return built AND
     */
    public StringFilter and(final Stream<StringFilter> operands) {
        final List<StringFilter> nonEmptyOperands = filterEmptyOperands(operands);
        if (nonEmptyOperands.size() == 1) {
            return nonEmptyOperands.get(0);
        } else {
            return new StringFilterAnd(nonEmptyOperands);
        }
    }

    /**
     * Get an OR expression.
     *
     * @param operands operands for the OR
     * @return built OR
     */
    public StringFilter or(final StringFilter... operands) {
        return or(Arrays.stream(operands));
    }

    /**
     * Get an OR expression.
     *
     * @param operands operands for the OR
     * @return built OR
     */
    public StringFilter or(final Stream<StringFilter> operands) {
        final List<StringFilter> nonEmptyOperands = filterEmptyOperands(operands);
        if (nonEmptyOperands.size() == 1) {
            return nonEmptyOperands.get(0);
        } else {
            return new StringFilterOr(nonEmptyOperands);
        }
    }

    /**
     * Get an NOT expression.
     *
     * @param operand operand to be negated
     * @return built NOT
     */
    public StringFilter not(final StringFilter operand) {
        return new StringFilterNot(operand);
    }

    private List<StringFilter> filterEmptyOperands(final Stream<StringFilter> operands) {
        return operands.filter(operand -> !IsEmptyVisitor.isEmpty(operand)).collect(Collectors.toList());
    }

    private static class IsEmptyVisitor implements StringFilterVisitor {
        private boolean isEmpty = false;

        public static boolean isEmpty(final StringFilter stringFilter) {
            final IsEmptyVisitor visitor = new IsEmptyVisitor();
            stringFilter.accept(visitor);
            return visitor.isEmpty;
        }

        @Override
        public void visit(final StringFilterAnd and) {
            visitLogicOperator(and.getOperands());
        }

        protected void visitLogicOperator(final List<StringFilter> operands) {
            this.isEmpty = operands.isEmpty() || operands.stream().allMatch(IsEmptyVisitor::isEmpty);
        }

        @Override
        public void visit(final StringFilterOr or) {
            visitLogicOperator(or.getOperands());
        }

        @Override
        public void visit(final StringFilterNot not) {
            // never empty
        }

        @Override
        public void visit(final WildcardExpression wildcardExpression) {
            // never empty
        }
    }
}
