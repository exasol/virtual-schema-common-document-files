package com.exasol.adapter.document.documentfetcher.files.segmentation;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

class HashSegmentMatcherTest {

    public static final HashSegmentDescription SEGMENT_1_DESCRIPTION = new HashSegmentDescription(2, 0);
    public static final HashSegmentDescription SEGMENT_2_DESCRIPTION = new HashSegmentDescription(2, 1);

    @Test
    void testPartionsAreMoreOrLessEqual() {
        final SegmentMatcher matcher = new HashSegmentMatcher(SEGMENT_1_DESCRIPTION);
        int matches = 0;
        final int numRuns = 1000;
        for (int counter = 0; counter < numRuns; counter++) {
            final String fileName = "my-file-" + counter + ".json";
            final RemoteFile remoteFile = getLoadedFileForName(fileName);
            if (matcher.matches(remoteFile)) {
                matches++;
            }
        }
        assertThat((double) matches, is(closeTo(numRuns / 2d, numRuns * 0.05)));
    }

    private RemoteFile getLoadedFileForName(final String fileName) {
        final RemoteFile remoteFile = mock(RemoteFile.class);
        when(remoteFile.getResourceName()).thenReturn(fileName);
        return remoteFile;
    }

    @Test
    void testPartitionsAreDisjoint() {
        final SegmentMatcher matcher1 = new HashSegmentMatcher(SEGMENT_1_DESCRIPTION);
        final SegmentMatcher matcher2 = new HashSegmentMatcher(SEGMENT_2_DESCRIPTION);
        for (int counter = 0; counter < 1000; counter++) {
            final String fileName = "my-file-" + counter + ".json";
            assertThat(matcher1.matches(getLoadedFileForName(fileName)),
                    not(equalTo(matcher2.matches(getLoadedFileForName(fileName)))));
        }
    }

}