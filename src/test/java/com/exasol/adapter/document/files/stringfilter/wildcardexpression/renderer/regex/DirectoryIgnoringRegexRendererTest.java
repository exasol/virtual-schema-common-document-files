package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.*;

class DirectoryIgnoringRegexRendererTest {
    private static final DirectoryIgnoringRegexRenderer RENDERER = new DirectoryIgnoringRegexRenderer();

    static Stream<Arguments> expectedRendering() {
        return Stream.of(//
                Arguments.of(new WildcardExpression(List.of(new DirectoryLimitedSingleCharWildcard())), "."), //
                Arguments.of(new WildcardExpression(List.of(new DirectoryLimitedMultiCharWildcard())), ".*"), //
                Arguments.of(new WildcardExpression(List.of(new CrossDirectorySingleCharWildcard())), "."), //
                Arguments.of(new WildcardExpression(List.of(new CrossDirectoryMultiCharWildcard())), ".*"), //
                Arguments.of(new WildcardExpression(List.of(new RegularCharacter('a'))), "\\Qa\\E"), //
                Arguments.of(WildcardExpression.fromGlob("a*b"), "\\Qa\\E.*\\Qb\\E"), //
                Arguments.of(WildcardExpression.fromLike("a%b", '\\'), "\\Qa\\E.*\\Qb\\E"), //
                Arguments.of(WildcardExpression.fromLike("a_b", '\\'), "\\Qa\\E.\\Qb\\E") //
        );
    }

    @MethodSource("expectedRendering")
    @ParameterizedTest
    void testRendering(final WildcardExpression expression, final String expectedResult) {
        assertThat(RENDERER.render(expression), equalTo(expectedResult));
    }
}