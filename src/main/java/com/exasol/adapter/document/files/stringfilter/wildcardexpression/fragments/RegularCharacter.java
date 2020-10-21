package com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragmentVisitor;

/**
 * This class describes regular characters in an expression.
 */
public class RegularCharacter implements WildcardExpressionFragment {
    private static final long serialVersionUID = 3923902784041914771L;

    /** @serial */
    private final char character;

    /**
     * Create a new instance of {@link RegularCharacter}.
     * 
     * @param character character to match
     */
    public RegularCharacter(final char character) {
        this.character = character;
    }

    /**
     * Get the character to match.
     * 
     * @return character to match
     */
    public char getCharacter() {
        return this.character;
    }

    @Override
    public void accept(final WildcardExpressionFragmentVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.valueOf(this.character);
    }
}
