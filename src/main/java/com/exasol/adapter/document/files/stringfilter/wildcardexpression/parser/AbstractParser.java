package com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.RegularCharacter;

/**
 * Abstract basis for parsers for {@link WildcardExpression}s.
 */
public abstract class AbstractParser {
    protected final char escapeChar;
    protected final char multiCharWildcard;
    protected final char singleCharWildcard;

    /**
     * Create a new instance of {@link AbstractParser}.
     * 
     * @param escapeChar         language specific escape character
     * @param multiCharWildcard  language specific wildcard that matches multiple characters
     * @param singleCharWildcard language specific wildcard that matches a single characters
     */
    public AbstractParser(final char escapeChar, final char multiCharWildcard, final char singleCharWildcard) {
        this.escapeChar = escapeChar;
        this.multiCharWildcard = multiCharWildcard;
        this.singleCharWildcard = singleCharWildcard;
    }

    /**
     * Parses an expression into a {@link WildcardExpression}.
     *
     * @param expression expression to parse
     * @return parsed {@link WildcardExpression}
     */
    public WildcardExpression parse(final String expression) {
        final List<WildcardExpressionFragment> fragments = new ArrayList<>();
        final EscapedState escapedState = new EscapedState();
        final Queue<Character> characterQueue = expression.chars().mapToObj(charValue -> (char) charValue)
                .collect(Collectors.toCollection(LinkedList::new));
        while (!characterQueue.isEmpty()) {
            handleNextCharacter(fragments, escapedState, characterQueue);
        }
        return new WildcardExpression(fragments);
    }

    private void handleNextCharacter(final List<WildcardExpressionFragment> fragments, final EscapedState escapedState,
            final Queue<Character> characterQueue) {
        final char currentChar = characterQueue.remove();
        if (currentChar == this.singleCharWildcard && escapedState.isUnescaped()) {
            fragments.add(getSingleCharWildcard(characterQueue));
        } else if (currentChar == this.multiCharWildcard && escapedState.isUnescaped()) {
            fragments.add(getMultiCharWildcard(characterQueue));
        } else if (currentChar == this.escapeChar && escapedState.isUnescaped()) {
            escapedState.setEscaped();
        } else {
            fragments.add(new RegularCharacter(currentChar));
            escapedState.clearEscaped();
        }
    }

    /**
     * Get the fitting single-character wildcard
     *
     * @param characterQueue remaining expression for look ahead
     * @return single-character wildcard
     */
    protected abstract WildcardExpressionFragment getSingleCharWildcard(final Queue<Character> characterQueue);

    /**
     * Get the fitting multi-character wildcard
     *
     * @param characterQueue remaining expression for look ahead
     * @return multi-character wildcard
     */
    protected abstract WildcardExpressionFragment getMultiCharWildcard(final Queue<Character> characterQueue);

    private static class EscapedState {
        private boolean isEscaped = false;

        public boolean isUnescaped() {
            return !this.isEscaped;
        }

        public void setEscaped() {
            this.isEscaped = true;
        }

        public void clearEscaped() {
            this.isEscaped = false;
        }
    }
}
