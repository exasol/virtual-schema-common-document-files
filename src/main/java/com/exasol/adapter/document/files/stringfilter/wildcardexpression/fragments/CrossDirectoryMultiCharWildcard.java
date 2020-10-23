package com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragmentVisitor;

/**
 * This class represent wildcards that match any number of characters including none and also matches slashes. By
 * that it also matches recursively into directory structures.
 */
public class CrossDirectoryMultiCharWildcard implements WildcardExpressionFragment {
    private static final long serialVersionUID = 7378976142816239029L;

    @Override
    public void accept(final WildcardExpressionFragmentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "<CrossDirectoryMultiCharWildcard>";
    }
}
