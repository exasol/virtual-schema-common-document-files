package com.exasol.adapter.document.documentfetcher.files.segmentation;

/**
 * This {@link SegmentDescription} segments a set of files by binning the hash sum of the filenames.
 */
public class HashSegmentDescription implements SegmentDescription {
    private static final long serialVersionUID = 4642659777595474828L;//
    /** @serial */
    private final int numberOfSegments;
    /** @serial */
    private final int segmentId;

    /**
     * Create a new instance of {@link HashSegmentDescription}.
     *
     * @param numberOfSegments total number of segments
     * @param segmentId        number of this segment
     */
    public HashSegmentDescription(final int numberOfSegments, final int segmentId) {
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

    @Override
    public void accept(final SegmentDescriptionVisitor visitor) {
        visitor.visit(this);
    }
}
