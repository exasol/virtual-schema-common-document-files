package com.exasol.adapter.document.documentfetcher.files.segmentation;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;

import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * This {@link SegmentMatcher} accepts all files since no segmentation is needed.
 */
public class NoSegmentationSegmentMatcher implements SegmentMatcher {
    @Override
    public List<FileSegment> getMatchingSegmentsFor(final RemoteFile remoteFile) {
        return List.of(new FileSegment(remoteFile, ENTIRE_FILE));
    }
}
