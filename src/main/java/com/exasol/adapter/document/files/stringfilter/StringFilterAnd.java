package com.exasol.adapter.document.files.stringfilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * And predicate for {@link StringFilter}s.
 */
public class StringFilterAnd implements StringFilter {
    private static final long serialVersionUID = 3856323687685172983L;
    /** @serial */
    private final ArrayList<StringFilter> operands;

    /**
     * Create a new instance of {@link StringFilterAnd}.
     * 
     * @param operands operands to combine with an AND.
     */
    StringFilterAnd(final List<StringFilter> operands) {
        this.operands = new ArrayList<>(operands);
    }

    @Override
    public String getStaticPrefix() {
        return this.operands.stream().map(StringFilter::getStaticPrefix).max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    @Override
    public boolean hasWildcard() {
        return this.operands.stream().allMatch(StringFilter::hasWildcard);
    }

    @Override
    public boolean hasContradiction() {
        if (this.operands.stream().anyMatch(StringFilter::hasContradiction)) {
            return true;
        } else {
            final List<String> prefixes = this.operands.stream().map(StringFilter::getStaticPrefix)
                    .collect(Collectors.toList());
            final String shortestPrefix = prefixes.stream().min(Comparator.comparingInt(String::length)).orElse("");
            return prefixes.stream().anyMatch(prefix -> !prefix.startsWith(shortestPrefix));
        }
    }

    @Override
    public void accept(final StringFilterVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get the operands of this AND.
     * 
     * @return operands
     */
    public List<StringFilter> getOperands() {
        return this.operands;
    }

    @Override
    public String toString() {
        return this.operands.stream().map(operand -> "(" + operand.toString() + ")")
                .collect(Collectors.joining(" AND "));
    }
}
