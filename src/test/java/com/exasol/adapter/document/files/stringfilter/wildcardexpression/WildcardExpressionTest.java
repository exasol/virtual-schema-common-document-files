package com.exasol.adapter.document.files.stringfilter.wildcardexpression;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WildcardExpressionTest {

    @CsvSource({ //
            "test%, test<CrossDirectoryMultiCharWildcard>", //
            "test_, test<CrossDirectorySingleCharWildcard>", //
            "test@_, test_", //
            "test@@_, test@<CrossDirectorySingleCharWildcard>", //
            "test@@@_, test@_", //
            "test@@@@_, test@@<CrossDirectorySingleCharWildcard>", //
            "test@@@@@_, test@@_", //
            "test@@@@@@_, test@@@<CrossDirectorySingleCharWildcard>", //
            "test@@@@@@@_, test@@@_" //
    })
    @ParameterizedTest
    void testParseLike(final String input, final String expectedOutput) {
        assertThat(WildcardExpression.fromLike(input, '@').toString(), equalTo(expectedOutput));
    }

    @CsvSource({ //
            "test\\_, test_", //
            "test\\\\_, test\\<CrossDirectorySingleCharWildcard>", //
    })
    @ParameterizedTest
    void testParseLikeTWithBackslashEscaptChar(final String input, final String expectedOutput) {
        assertThat(WildcardExpression.fromLike(input, '\\').toString(), equalTo(expectedOutput));
    }

    @CsvSource({ //
            "test, false", //
            "test%, true", //
            "test_, true", //
            "_test_, true", //
    })
    @ParameterizedTest
    void testHasWildcard(final String likeInput, final boolean expectedResult) {
        assertThat(WildcardExpression.fromLike(likeInput, '@').hasWildcard(), equalTo(expectedResult));
    }

    @CsvSource({ //
            "test, \\Qtest\\E", //
            "test*, \\Qtest\\E.*", //
            "test-?.json, \\Qtest-\\E.\\Q.json\\E", //
    })
    @ParameterizedTest
    void testConvertGlobToRegex(final String glob, final String regex) {
        assertThat(WildcardExpression.fromGlob(glob).asDirectoryIgnoringRegex(), equalTo(regex));
    }
}