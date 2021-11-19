package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * Interface for classes that separates a set of files into multiple segments.
 */
public interface SegmentMatcher {
    /**
     * Matches files that belong to the {@link SegmentDescription} passed to the constructor.
     *
     * @param remoteFile file to match
     * @return List of segments of that file that matched. Empty list if file did not match.
     */
    List<FileSegment> getMatchingSegmentsFor(RemoteFile remoteFile);
}
