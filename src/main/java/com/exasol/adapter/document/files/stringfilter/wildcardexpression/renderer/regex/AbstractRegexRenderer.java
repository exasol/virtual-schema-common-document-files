package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import java.util.regex.Pattern;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpressionFragmentVisitor;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.*;

/**
 * Abstract basis for regex renderer.
 */
public abstract class AbstractRegexRenderer implements RegexRenderer {
    private final String singleCharWildcard;

    /**
     * Create a new instance of {@link AbstractRegexRenderer}.
     * 
     * @param singleCharWildcard wildcard that matches a single character
     */
    protected AbstractRegexRenderer(final String singleCharWildcard) {
        this.singleCharWildcard = singleCharWildcard;
    }

    @Override
    public String render(final WildcardExpression expression) {
        final RegexBuilder regexBuilder = new RegexBuilder(this.singleCharWildcard);
        expression.getFragments().forEach(fragment -> fragment.accept(regexBuilder));
        return regexBuilder.getRegex();
    }

    private static class RegexBuilder implements WildcardExpressionFragmentVisitor {
        private final String singleCharWildcard;

        private final StringBuilder regexStringBuilder = new StringBuilder();
        private StringBuilder quotedBuilder = new StringBuilder();

        protected RegexBuilder(final String singleCharWildcard) {

            this.singleCharWildcard = singleCharWildcard;
        }

        private void flushQuoted() {
            final String stringToQuote = this.quotedBuilder.toString();
            this.quotedBuilder = new StringBuilder();
            if (!stringToQuote.isEmpty()) {
                this.regexStringBuilder.append(Pattern.quote(stringToQuote));
            }
        }

        @Override
        public void visit(final DirectoryLimitedMultiCharWildcard wildcard) {
            flushQuoted();
            this.regexStringBuilder.append(this.singleCharWildcard).append("*");
        }

        @Override
        public void visit(final CrossDirectoryMultiCharWildcard wildcard) {
            flushQuoted();
            this.regexStringBuilder.append(".*");
        }

        @Override
        public void visit(final CrossDirectorySingleCharWildcard wildcard) {
            flushQuoted();
            this.regexStringBuilder.append(".");
        }

        @Override
        public void visit(final DirectoryLimitedSingleCharWildcard wildcard) {
            flushQuoted();
            this.regexStringBuilder.append(this.singleCharWildcard);
        }

        @Override
        public void visit(final RegularCharacter character) {
            this.quotedBuilder.append(character.getCharacter());
        }

        /**
         * Get the built regular expression.
         *
         * @return built regular expression
         */
        public String getRegex() {
            flushQuoted();
            return this.regexStringBuilder.toString();
        }
    }
}
