package com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragmentVisitor;

/**
 * This class represents a wildcard that matches exactly one character, but does not match the directory separator.
 */
public class DirectoryLimitedSingleCharWildcard implements WildcardExpressionFragment {
    private static final long serialVersionUID = 1947044901719175233L;

    @Override
    public void accept(final WildcardExpressionFragmentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "<DirectoryLimitedSingleCharWildcard>";
    }
}
