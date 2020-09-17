package com.exasol.adapter.document.documentfetcher.files;

import java.io.Serializable;

/**
 * This class describes a segment of data.
 */
public class SegmentDescription implements Serializable {
    public static final SegmentDescription NO_SEGMENTATION = new SegmentDescription(1, 0);
    private static final long serialVersionUID = -8482568518621558183L;
    private final int numSegments;
    private final int segmentId;

    /**
     * Create a new instance of {@link SegmentDescription}.
     *
     * @param numSegments total number of segments
     * @param segmentId   number of this segment
     */
    public SegmentDescription(final int numSegments, final int segmentId) {
        this.numSegments = numSegments;
        this.segmentId = segmentId;
    }

    /**
     * Get the total number of segments.
     * 
     * @return total number iof segments
     */
    public int getNumSegments() {
        return this.numSegments;
    }

    /**
     * Get this segment
     * 
     * @return this segment
     */
    public int getSegmentId() {
        return this.segmentId;
    }
}
