package com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GlobParserTest {

    @CsvSource({ //
            "test*, test<DirectoryLimitedMultiCharWildcard>", //
            "test?, test<DirectoryLimitedSingleCharWildcard>", //
            "test**, test<CrossDirectoryMultiCharWildcard>", //
            "test**test, test<CrossDirectoryMultiCharWildcard>test", //
            "test***, test<CrossDirectoryMultiCharWildcard><DirectoryLimitedMultiCharWildcard>",//
    })
    @ParameterizedTest
    void testParse(final String globExpression, final String expectedResult) {
        assertThat(new GlobParser().parse(globExpression).toString(), equalTo(expectedResult));
    }
}