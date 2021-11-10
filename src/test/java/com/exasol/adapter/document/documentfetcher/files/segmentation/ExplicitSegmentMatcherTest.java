package com.exasol.adapter.document.documentfetcher.files.segmentation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

class ExplicitSegmentMatcherTest {

    @Test
    void testMatch() {
        final RemoteFile remoteFile = getRemoteFileForName("test.txt");
        final ExplicitSegmentDescription segmentDescription = new ExplicitSegmentDescription(List.of(remoteFile));
        assertTrue(new ExplicitSegmentMatcher(segmentDescription).matches(remoteFile));
    }

    @Test
    void testMismatch() {
        final ExplicitSegmentDescription segmentDescription = new ExplicitSegmentDescription(
                List.of(getRemoteFileForName("test.txt")));
        assertFalse(new ExplicitSegmentMatcher(segmentDescription).matches(getRemoteFileForName("other.txt")));
    }

    private RemoteFile getRemoteFileForName(final String fileName) {
        final RemoteFile remoteFile = mock(RemoteFile.class);
        when(remoteFile.getResourceName()).thenReturn(fileName);
        return remoteFile;
    }
}