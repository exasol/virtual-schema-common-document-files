package com.exasol.adapter.document.files.stringfilter.wildcardexpression.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.fragments.RegularCharacter;

class NonWildcardParserTest {

    @Test
    void testParsing() {
        final NonWildcardParser parser = new NonWildcardParser();
        final String testString = "test1234*_+-%";
        final WildcardExpression expression = parser.parse(testString);
        assertAll(//
                () -> assertThat(expression.getFragments().size(), equalTo(testString.length())),
                () -> assertThat(
                        expression.getFragments().stream().allMatch(fragment -> fragment instanceof RegularCharacter),
                        equalTo(true))//
        );
    }
}