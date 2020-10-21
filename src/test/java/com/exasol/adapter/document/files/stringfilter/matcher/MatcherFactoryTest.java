package com.exasol.adapter.document.files.stringfilter.matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.files.stringfilter.StringFilterFactory;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex.DirectoryIgnoringRegexRenderer;

class MatcherFactoryTest {
    private static final MatcherFactory FACTORY = new MatcherFactory(new DirectoryIgnoringRegexRenderer());

    @CsvSource({ //
            "c, false", //
            "a, true", //
    })
    @ParameterizedTest
    void testMatchWildcard(final String input, final boolean expectedResult) {
        final Matcher matcher = FACTORY.getMatcher(WildcardExpression.fromGlob("a*"));
        assertThat(matcher.matches(input), equalTo(expectedResult));
    }

    @CsvSource({ //
            "c, false", //
            "a, true", //
            "b, true", //
    })
    @ParameterizedTest
    void testMatchOr(final String input, final boolean expectedResult) {
        final Matcher matcher = FACTORY.getMatcher(
                new StringFilterFactory().or(WildcardExpression.fromGlob("a*"), WildcardExpression.fromGlob("b*")));
        assertThat(matcher.matches(input), equalTo(expectedResult));
    }

    @CsvSource({ //
            "a, false", //
            "b, false", //
            "ab, true", //
    })
    @ParameterizedTest
    void testMatchAnd(final String input, final boolean expectedResult) {
        final Matcher matcher = FACTORY.getMatcher(
                new StringFilterFactory().and(WildcardExpression.fromGlob("a*"), WildcardExpression.fromGlob("*b")));
        assertThat(matcher.matches(input), equalTo(expectedResult));
    }

    @CsvSource({ //
            "b, true", //
            "a, false", //
    })
    @ParameterizedTest
    void testMatchNot(final String input, final boolean expectedResult) {
        final Matcher matcher = FACTORY.getMatcher(new StringFilterFactory().not(WildcardExpression.fromGlob("a*")));
        assertThat(matcher.matches(input), equalTo(expectedResult));
    }
}