package com.exasol.adapter.document.documentfetcher.files;

import java.io.Serializable;

/**
 * This class describes a segment of data.
 */
public class SegmentDescription implements Serializable {
    public static final SegmentDescription NO_SEGMENTATION = new SegmentDescription(1, 0);
    private static final long serialVersionUID = -6391949745846220794L;//
    /** @serial */
    private final int numberOfSegments;
    /** @serial */
    private final int segmentId;

    /**
     * Create a new instance of {@link SegmentDescription}.
     *
     * @param numberOfSegments total number of segments
     * @param segmentId        number of this segment
     */
    public SegmentDescription(final int numberOfSegments, final int segmentId) {
        this.numberOfSegments = numberOfSegments;
        this.segmentId = segmentId;
    }

    /**
     * Get the total number of segments.
     * 
     * @return total number iof segments
     */
    public int getNumberOfSegments() {
        return this.numberOfSegments;
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
