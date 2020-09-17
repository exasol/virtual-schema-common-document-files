package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import org.junit.jupiter.api.Test;

class SegmentMatcherTest {

    public static final SegmentDescription SEGMENT_1_DESCRIPTION = new SegmentDescription(2, 0);
    public static final SegmentDescription SEGMENT_2_DESCRIPTION = new SegmentDescription(2, 1);

    @Test
    void testPartionsAreMoreOrLessEqual() {
        final SegmentMatcher matcher = new SegmentMatcher(SEGMENT_1_DESCRIPTION);
        int matches = 0;
        final int numRuns = 1000;
        for (int counter = 0; counter < numRuns; counter++) {
            final String fileName = "my-file-" + counter + ".json";
            if (matcher.matches(fileName)) {
                matches++;
            }
        }
        assertThat((double) matches, is(closeTo(numRuns / 2d, numRuns * 0.05)));
    }

    @Test
    void testPartitionsAreDisjoint() {
        final SegmentMatcher matcher1 = new SegmentMatcher(SEGMENT_1_DESCRIPTION);
        final SegmentMatcher matcher2 = new SegmentMatcher(SEGMENT_2_DESCRIPTION);
        for (int counter = 0; counter < 1000; counter++) {
            final String fileName = "my-file-" + counter + ".json";
            assertThat(matcher1.matches(fileName), not(equalTo(matcher2.matches(fileName))));
        }
    }

}