package com.exasol.adapter.document.files.stringfilter.wildcardexpression;

import java.io.Serializable;

/**
 * Fragment of a {@link WildcardExpression}.
 */
public interface WildcardExpressionFragment extends Serializable {

    /**
     * Accept a {@link WildcardExpressionFragmentVisitor}.
     * 
     * @param visitor visitor to accept
     */
    void accept(WildcardExpressionFragmentVisitor visitor);
}
