package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * This class matches part descriptions of a file against a {@link FileSegmentDescription}.
 */
@RequiredArgsConstructor
public class FileSegmentDescriptionMatcher {
    private final FileSegmentDescription segmentDescription;

    /**
     * Matches part descriptions of a file against a {@link FileSegmentDescription}.
     * 
     * @param splits list of parts
     * @param <T>    type of the part descriptions
     * @return matched parts
     */
    public <T> List<T> filter(final List<T> splits) {
        final int numberOfSegments = this.segmentDescription.getNumberOfSegments();
        final double segmentSize = (double) splits.size() / (double) numberOfSegments;
        final int segmentId = this.segmentDescription.getSegmentId();
        final int start = (int) Math.round(segmentId * segmentSize);
        final int end = (int) Math.round((segmentId + 1) * segmentSize);
        return splits.subList(start, end);
    }
}
