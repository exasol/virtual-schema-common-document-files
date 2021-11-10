package com.exasol.adapter.document.documentfetcher.files.segmentation;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SegmentMatcherFactoryTest {

    static Stream<Arguments> getBuildMatcherCases() {
        return Stream.of(//
                Arguments.of(new ExplicitSegmentDescription(Collections.emptyList()), ExplicitSegmentMatcher.class), //
                Arguments.of(new HashSegmentDescription(2, 1), HashSegmentMatcher.class)//
        );
    }

    @ParameterizedTest
    @MethodSource("getBuildMatcherCases")
    void testBuildMatcher(final SegmentDescription segmentDescription, final Class<?> expectedClass) {
        final SegmentMatcher segmentMatcher = SegmentMatcherFactory.buildSegmentMatcher(segmentDescription);
        assertThat(segmentMatcher, Matchers.instanceOf(expectedClass));
    }
}