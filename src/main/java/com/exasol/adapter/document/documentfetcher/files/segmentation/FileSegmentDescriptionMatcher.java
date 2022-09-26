package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.List;

/**
 * This class matches part descriptions of a file against a {@link FileSegmentDescription}.
 */
public class FileSegmentDescriptionMatcher {
    private final FileSegmentDescription segmentDescription;

    /**
     * Create a new {@link FileSegmentDescriptionMatcher}.
     * 
     * @param segmentDescription the segment description
     */
    public FileSegmentDescriptionMatcher(final FileSegmentDescription segmentDescription) {
        this.segmentDescription = segmentDescription;
    }

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
