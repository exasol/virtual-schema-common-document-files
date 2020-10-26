package com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser;

import java.util.Queue;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragment;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.CrossDirectoryMultiCharWildcard;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.CrossDirectorySingleCharWildcard;

/**
 * This class parses a LIKE expression into a {@link WildcardExpression}.
 */
public class LikeParser extends AbstractParser {

    /**
     * Create a new instance of {@link LikeParser}.
     * 
     * @param likeEscapeChar Escape character fot the LIKE expressions
     */
    public LikeParser(final char likeEscapeChar) {
        super(likeEscapeChar, '%', '_');
    }

    @Override
    protected WildcardExpressionFragment getSingleCharWildcard(final Queue<Character> characterQueue) {
        return new CrossDirectorySingleCharWildcard();
    }

    @Override
    protected WildcardExpressionFragment getMultiCharWildcard(final Queue<Character> characterQueue) {
        return new CrossDirectoryMultiCharWildcard();
    }
}
