package com.exasol.adapter.document.files.stringfilter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;

/**
 * This class prepends a static prefix to a {@link StringFilter}.
 */
public class PrefixPrepender {

    /**
     * Prepend a static prefix to a given {@link StringFilter}.
     * 
     * @param prefix  prefix to prepend
     * @param subject {@link StringFilter} to prepend to
     * @return new {@link StringFilter}
     */
    public StringFilter prependStaticPrefix(final String prefix, final StringFilter subject) {
        final PrependingVisitor visitor = new PrependingVisitor(this, prefix);
        subject.accept(visitor);
        return visitor.getResult();
    }

    private static class PrependingVisitor implements StringFilterVisitor {
        private final PrefixPrepender prepender;
        private final String prefix;
        private StringFilter result;

        private PrependingVisitor(final PrefixPrepender prepender, final String prefix) {
            this.prepender = prepender;
            this.prefix = prefix;
        }

        @Override
        public void visit(final StringFilterAnd and) {
            this.result = new StringFilterFactory().and(and.getOperands().stream()
                    .map(operand -> this.prepender.prependStaticPrefix(this.prefix, operand)));
        }

        @Override
        public void visit(final StringFilterOr or) {
            this.result = new StringFilterFactory().or(
                    or.getOperands().stream().map(operand -> this.prepender.prependStaticPrefix(this.prefix, operand)));
        }

        @Override
        public void visit(final StringFilterNot not) {
            this.result = new StringFilterNot(this.prepender.prependStaticPrefix(this.prefix, not.getOperand()));
        }

        @Override
        public void visit(final WildcardExpression wildcardExpression) {
            final List<WildcardExpressionFragment> fragments = Stream
                    .concat(WildcardExpression.forNonWildcardString(this.prefix).getFragments().stream(),
                            wildcardExpression.getFragments().stream())
                    .collect(Collectors.toList());
            this.result = new WildcardExpression(fragments);
        }

        private StringFilter getResult() {
            return this.result;
        }
    }
}
