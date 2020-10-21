package com.exasol.adapter.document.files.stringfilter.matcher;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex.RegexRenderer;

/**
 * This class builds {@link Matcher}s for {@link StringFilter}s.
 */
public class MatcherFactory {
    private final RegexRenderer regexRenderer;

    /**
     * Create a new instance of {@link MatcherFactory}.
     * 
     * @param regexRenderer the {@link RegexRenderer} to use for converting {@link WildcardExpression} into regular
     *                      expressions.
     */
    public MatcherFactory(final RegexRenderer regexRenderer) {
        this.regexRenderer = regexRenderer;
    }

    /**
     * Get a {@link Matcher} for a given {@link StringFilter}.
     * 
     * @param stringFilter {@link StringFilter} to build the {@link Matcher} for
     * @return built {@link Matcher}.
     */
    public Matcher getMatcher(final StringFilter stringFilter) {
        final MatcherBuildingVisitor visitor = new MatcherBuildingVisitor(this);
        stringFilter.accept(visitor);
        return visitor.getBuiltMatcher();
    }

    private static class MatcherBuildingVisitor implements StringFilterVisitor {
        private final MatcherFactory matcherFactory;
        private Matcher builtMatcher;

        private MatcherBuildingVisitor(final MatcherFactory matcherFactory) {
            this.matcherFactory = matcherFactory;
        }

        @Override
        public void visit(final StringFilterAnd and) {
            final List<Matcher> operandsMatchers = and.getOperands().stream().map(this.matcherFactory::getMatcher)
                    .collect(Collectors.toList());
            this.builtMatcher = stringToMatch -> operandsMatchers.stream()
                    .allMatch(matcher -> matcher.matches(stringToMatch));
        }

        @Override
        public void visit(final StringFilterOr or) {
            final List<Matcher> operandsMatchers = or.getOperands().stream().map(this.matcherFactory::getMatcher)
                    .collect(Collectors.toList());
            this.builtMatcher = stringToMatch -> operandsMatchers.stream()
                    .anyMatch(matcher -> matcher.matches(stringToMatch));
        }

        @Override
        public void visit(final StringFilterNot not) {
            final Matcher operandsMatcher = this.matcherFactory.getMatcher(not.getOperand());
            this.builtMatcher = stringToMatch -> !operandsMatcher.matches(stringToMatch);
        }

        @Override
        public void visit(final WildcardExpression wildcardExpression) {
            final Pattern regexPattern = Pattern.compile(this.matcherFactory.regexRenderer.render(wildcardExpression));
            this.builtMatcher = stringToMatch -> regexPattern.matcher(stringToMatch).matches();
        }

        public Matcher getBuiltMatcher() {
            return this.builtMatcher;
        }
    }
}
