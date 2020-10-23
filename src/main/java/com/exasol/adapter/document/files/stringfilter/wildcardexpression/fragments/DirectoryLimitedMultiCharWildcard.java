package com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragmentVisitor;

/**
 * This class represent wildcards that match any number of characters including none, but it is limited to one
 * path segment. So in directory structures it does not match recursively.
 */
public class DirectoryLimitedMultiCharWildcard implements WildcardExpressionFragment {
    private static final long serialVersionUID = 3700416607925554441L;

    @Override
    public void accept(final WildcardExpressionFragmentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "<DirectoryLimitedMultiCharWildcard>";
    }
}
