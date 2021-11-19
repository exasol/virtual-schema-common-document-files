package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileSegmentDescriptionMatcher {
    private final FileSegmentDescription segmentDescription;

    public <T> List<T> filter(final List<T> splits) {
        final int numberOfSegments = this.segmentDescription.getNumberOfSegments();
        final double segmentSize = (double) splits.size() / (double) numberOfSegments;
        final int segmentId = this.segmentDescription.getSegmentId();
        final int start = (int) Math.round(segmentId * segmentSize);
        final int end = (int) Math.round((segmentId + 1) * segmentSize);
        return splits.subList(start, end);
    }
}
