package com.exasol.adapter.document.files.stringfilter;

/**
 * NOT predicate for {@link StringFilter}.
 */
public class StringFilterNot implements StringFilter {
    private static final long serialVersionUID = -8313963115014264939L;
    /** @serial */
    private final StringFilter operand;

    /**
     * Create a new instance of {@link StringFilterNot}.
     * 
     * @param operand operand to negate
     */
    StringFilterNot(final StringFilter operand) {
        this.operand = operand;
    }

    @Override
    public String getStaticPrefix() {
        return "";
    }

    @Override
    public boolean hasWildcard() {
        return true;
    }

    @Override
    public boolean hasContradiction() {
        return false;
    }

    @Override
    public void accept(final StringFilterVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NOT(" + this.operand.toString() + ")";
    }

    /**
     * Get the operand to negate.
     * 
     * @return operand
     */
    public StringFilter getOperand() {
        return this.operand;
    }
}
