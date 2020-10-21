package com.exasol.adapter.document.files.stringfilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

class PrefixPrependerTest {
    private static final PrefixPrepender PREPENDER = new PrefixPrepender();

    @Test
    void testPrependToWildcardExpression() {
        final StringFilter result = PREPENDER.prependStaticPrefix("prefix/",
                WildcardExpression.forNonWildcardString("test"));
        assertThat(result.toString(), equalTo("prefix/test"));
    }

    @Test
    void testPrependToAnd() {
        final StringFilter result = PREPENDER.prependStaticPrefix("prefix/", new StringFilterFactory().and(
                WildcardExpression.forNonWildcardString("test1"), WildcardExpression.forNonWildcardString("test2")));
        assertThat(result.toString(), equalTo("(prefix/test1) AND (prefix/test2)"));
    }

    @Test
    void testPrependToOr() {
        final StringFilter result = PREPENDER.prependStaticPrefix("prefix/", new StringFilterFactory().or(
                WildcardExpression.forNonWildcardString("test1"), WildcardExpression.forNonWildcardString("test2")));
        assertThat(result.toString(), equalTo("(prefix/test1) OR (prefix/test2)"));
    }

    @Test
    void testPrependToNot() {
        final StringFilter result = PREPENDER.prependStaticPrefix("prefix/",
                new StringFilterFactory().not(WildcardExpression.forNonWildcardString("test")));
        assertThat(result.toString(), equalTo("NOT(prefix/test)"));
    }
}