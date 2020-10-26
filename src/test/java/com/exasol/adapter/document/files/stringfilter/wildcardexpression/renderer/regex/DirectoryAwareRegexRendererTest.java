package com.exasol.adapter.document.files.stringfilter.wildcardexpression.renderer.regex;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.CrossDirectoryMultiCharWildcard;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.CrossDirectorySingleCharWildcard;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.DirectoryLimitedMultiCharWildcard;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.DirectoryLimitedSingleCharWildcard;

class DirectoryAwareRegexRendererTest {

    private static final DirectoryAwareRegexRenderer RENDERER = new DirectoryAwareRegexRenderer("/");

    static Stream<Arguments> expectedRendering() {
        return Stream.of(//
                Arguments.of(new WildcardExpression(List.of(new DirectoryLimitedSingleCharWildcard())), "[^\\Q/\\E]"), //
                Arguments.of(new WildcardExpression(List.of(new DirectoryLimitedMultiCharWildcard())), "[^\\Q/\\E]*"), //
                Arguments.of(new WildcardExpression(List.of(new CrossDirectorySingleCharWildcard())), "."), //
                Arguments.of(new WildcardExpression(List.of(new CrossDirectoryMultiCharWildcard())), ".*") //
        );
    }

    @MethodSource("expectedRendering")
    @ParameterizedTest
    void testRendering(final WildcardExpression expression, final String expectedResult) {
        assertThat(RENDERER.render(expression), equalTo(expectedResult));
    }
}