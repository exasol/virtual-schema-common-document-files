package com.exasol.adapter.document.files.stringfilter;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Visitor interface for {@link StringFilter}.
 */
public interface StringFilterVisitor {
    public void visit(StringFilterAnd and);

    public void visit(StringFilterOr or);

    public void visit(StringFilterNot not);

    public void visit(WildcardExpression wildcardExpression);
}
