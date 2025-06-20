package com.exasol.adapter.document.files.stringfilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class StringFilterAndTest {

    @ValueSource(booleans = { true, false })
    @ParameterizedTest
    void testHasWildcardWithOneWildcard(final boolean hasWildcard) {
        final StringFilterAnd logicOperator = new StringFilterAnd(List.of(mockFilter(hasWildcard)));
        assertThat(logicOperator.hasWildcard(), equalTo(hasWildcard));
    }

    @CsvSource({ //
            "false,false", //
            "false,true", //
            "true,false", //
            "true,true",//
    })
    @ParameterizedTest
    void testHasWildcardWithTwoWildcards(final boolean hasWildcard1, final boolean hasWildcard2) {
        final StringFilterAnd logicOperator = new StringFilterAnd(
                List.of(mockFilter(hasWildcard1), mockFilter(hasWildcard2)));
        assertThat(logicOperator.hasWildcard(), equalTo(hasWildcard1 && hasWildcard2));
    }

    private StringFilter mockFilter(final boolean hasWildcard) {
        final StringFilter stringFilter = mock(StringFilter.class);
        when(stringFilter.hasWildcard()).thenReturn(hasWildcard);
        return stringFilter;
    }

    @CsvSource(value = { //
            "a, a, a", //
            "ab, a, ab", //
            "a, ab, ab",//
    })
    @ParameterizedTest
    void testGetStaticPrefix(final String filter1, final String filter2, final String expectedResult) {
        final StringFilter stringFilter1 = mock(StringFilter.class);
        when(stringFilter1.getStaticPrefix()).thenReturn(filter1);
        final StringFilter stringFilter2 = mock(StringFilter.class);
        when(stringFilter2.getStaticPrefix()).thenReturn(filter2);
        assertThat(new StringFilterAnd(List.of(stringFilter1, stringFilter2)).getStaticPrefix(),
                equalTo(expectedResult));
    }

    @CsvSource({ //
            "a, a, false", //
            "a, ab, false", //
            "ab, a, false", //
            "a, b, true", //
            "ac, ab, true",//
    })
    @ParameterizedTest
    void testHasContradiction(final String filter1, final String filter2, final boolean expectedResult) {
        final StringFilter stringFilter1 = mock(StringFilter.class);
        when(stringFilter1.getStaticPrefix()).thenReturn(filter1);
        final StringFilter stringFilter2 = mock(StringFilter.class);
        when(stringFilter2.getStaticPrefix()).thenReturn(filter2);
        final List<StringFilter> filters = List.of(stringFilter1, stringFilter2);
        final StringFilterAnd filter = new StringFilterAnd(filters);
        assertThat(filter.hasContradiction(), equalTo(expectedResult));
        if (expectedResult) {
            final String shortestPrefix = filter.getShortestPrefix();
            final String longestPrefix = filter.getStaticPrefix();
            final List<String> prefixes = filter.getPrefixes();
            String contradictionLogMessage = filter.getContradictionLogMessage(prefixes, shortestPrefix, longestPrefix);
            List<String> conflictingPrefixes = filter.getConflictingPrefixes(prefixes, shortestPrefix);
            String expectedLogMessage = String.format(
                    "Contradiction detected in StringFilter. Expected all prefixes to start with the shortest prefix: " +
                            "[The shorted prefix = '%s'], " +
                            "[The longest prefix = '%s'], " +
                            "[Conflicting prefixes = '%s'], " +
                            "[Full list of prefixes = '%s']. " +
                            "Returning EmptyQueryPlan.",
                    shortestPrefix,
                    longestPrefix,
                    conflictingPrefixes,
                    prefixes
            );
            assertThat(contradictionLogMessage, equalTo(expectedLogMessage));
        }
    }

    @ValueSource(booleans = { true, false })
    @ParameterizedTest
    void testHasContradictionIfOneOperandHas(final boolean hasContradiction) {
        final StringFilter stringFilter1 = mock(StringFilter.class);
        when(stringFilter1.hasContradiction()).thenReturn(hasContradiction);
        when(stringFilter1.getStaticPrefix()).thenReturn("a");
        assertThat(new StringFilterAnd(List.of(stringFilter1)).hasContradiction(), equalTo(hasContradiction));
    }
}