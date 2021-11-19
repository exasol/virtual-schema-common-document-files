package com.exasol.adapter.document.documentfetcher.files.segmentation;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

class ExplicitSegmentMatcherTest {

    @Test
    void testMatchWholeFile() {
        final RemoteFile remoteFile = getRemoteFileForName("test.txt");
        final FileSegment segment = new FileSegment(remoteFile, ENTIRE_FILE);
        final ExplicitSegmentDescription segmentDescription = new ExplicitSegmentDescription(List.of(segment));
        final List<FileSegment> result = new ExplicitSegmentMatcher(segmentDescription)
                .getMatchingSegmentsFor(remoteFile);
        assertThat(result, contains(segment));
    }

    @Test
    void testMatchSegmentsFile() {
        final RemoteFile remoteFile = getRemoteFileForName("test.txt");
        final FileSegment segment1 = new FileSegment(remoteFile, new FileSegmentDescription(3, 0));
        final FileSegment segment2 = new FileSegment(remoteFile, new FileSegmentDescription(3, 1));
        final ExplicitSegmentDescription segmentDescription = new ExplicitSegmentDescription(
                List.of(segment1, segment2));
        final List<FileSegment> result = new ExplicitSegmentMatcher(segmentDescription)
                .getMatchingSegmentsFor(remoteFile);
        assertThat(result, containsInAnyOrder(segment1, segment2));
    }

    @Test
    void testMismatch() {
        final ExplicitSegmentDescription segmentDescription = new ExplicitSegmentDescription(
                List.of(new FileSegment(getRemoteFileForName("test.txt"), ENTIRE_FILE)));
        final List<FileSegment> result = new ExplicitSegmentMatcher(segmentDescription)
                .getMatchingSegmentsFor(getRemoteFileForName("other.txt"));
        assertThat(result, empty());
    }

    private RemoteFile getRemoteFileForName(final String fileName) {
        final RemoteFile remoteFile = mock(RemoteFile.class);
        when(remoteFile.getResourceName()).thenReturn(fileName);
        return remoteFile;
    }
}