package com.exasol.adapter.document.files.stringfilter;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Visitor interface for {@link StringFilter}.
 */
public interface StringFilterVisitor {
    /**
     * Visit {@link StringFilterAnd}.
     * 
     * @param and {@link StringFilterAnd} to visit
     */
    public void visit(StringFilterAnd and);

    /**
     * Visit {@link StringFilterOr}.
     *
     * @param or {@link StringFilterOr} to visit
     */
    public void visit(StringFilterOr or);

    /**
     * Visit {@link StringFilterNot}.
     *
     * @param not {@link StringFilterNot} to visit
     */
    public void visit(StringFilterNot not);

    /**
     * Visit {@link WildcardExpression}.
     *
     * @param wildcardExpression {@link WildcardExpression} to visit
     */
    public void visit(WildcardExpression wildcardExpression);
}
