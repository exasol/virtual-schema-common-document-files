package com.exasol.adapter.document.files.stringfilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class StringFilterNotTest {

    public static final StringFilterNot NOT = new StringFilterNot(mock(StringFilter.class));

    @Test
    void testHasContradiction() {
        assertThat(NOT.hasContradiction(), equalTo(false));
    }

    @Test
    void testGetStaticPrefix() {
        assertThat(NOT.getStaticPrefix(), equalTo(""));
    }

    @Test
    void testHasWildcard() {
        assertThat(NOT.hasWildcard(), equalTo(true));
    }
}