package com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.RegularCharacter;

/**
 * This class build {@link WildcardExpression}s for strings without wildcards.
 */
public class NonWildcardParser {

    /**
     * Create a {@link WildcardExpression}s from a string without wildcards.
     * <p>
     * If the input string contains characters that are used as wildcard characters in some regular language, they are
     * treated as normal characters. The renderer will then escape them.
     * </p>
     * 
     * @param regularString string without wildcards.
     * @return built {@link WildcardExpression}.
     */
    public WildcardExpression parse(final String regularString) {
        final List<WildcardExpressionFragment> fragments = new ArrayList<>(regularString.length());
        for (int characterPointer = 0; characterPointer < regularString.length(); characterPointer++) {
            final char currentChar = regularString.charAt(characterPointer);
            fragments.add(new RegularCharacter(currentChar));
        }
        return new WildcardExpression(fragments);
    }
}
