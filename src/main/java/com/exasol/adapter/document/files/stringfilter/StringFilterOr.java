package com.exasol.adapter.document.files.stringfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OR predicate for {@link StringFilter}.
 */
public class StringFilterOr implements StringFilter {
    private static final long serialVersionUID = -5480465940663077807L;
    /** @serial */
    private final ArrayList<StringFilter> operands;

    /**
     * Create a new instance of {@clink StringFilterOr}.
     * 
     * @param operands operands to be combined with OR semantic
     */
    StringFilterOr(final List<StringFilter> operands) {
        this.operands = new ArrayList<>(operands);
    }

    @Override
    public boolean hasWildcard() {
        return this.operands.stream().anyMatch(StringFilter::hasWildcard);
    }

    @Override
    public boolean hasContradiction() {
        return this.operands.stream().allMatch(StringFilter::hasContradiction);
    }

    @Override
    public void accept(final StringFilterVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getStaticPrefix() {
        final List<String> operandsPrefixes = this.operands.stream().map(StringFilter::getStaticPrefix)
                .collect(Collectors.toList());
        final StringBuilder prefixBuilder = new StringBuilder();
        final int shortestOperandsPrefixLength = operandsPrefixes.stream().map(String::length).min(Integer::compareTo)
                .orElse(-1);
        for (int cursor = 0; cursor < shortestOperandsPrefixLength; cursor++) {
            final int currentCursor = cursor;
            final char currentChar = operandsPrefixes.stream().findAny().orElse("").charAt(currentCursor);
            if (operandsPrefixes.stream().anyMatch(prefix -> prefix.charAt(currentCursor) != currentChar)) {
                break;
            } else {
                prefixBuilder.append(currentChar);
            }
        }
        return prefixBuilder.toString();
    }

    /**
     * Get the operands of this OR.
     * 
     * @return operands
     */
    public List<StringFilter> getOperands() {
        return this.operands;
    }

    @Override
    public String toString() {
        return this.operands.stream().map(operand -> "(" + operand.toString() + ")")
                .collect(Collectors.joining(" OR "));
    }
}
