package com.exasol.adapter.document.documentfetcher.files.segmentation;

/**
 * Visitor for {@link SegmentDescription}s.
 */
public interface SegmentDescriptionVisitor {

    /**
     * Visit a {@link HashSegmentDescription}
     * 
     * @param segmentDescription {@link HashSegmentDescription} to visit
     */
    public void visit(HashSegmentDescription segmentDescription);

    /**
     * Visit an {@link ExplicitSegmentDescription}
     *
     * @param segmentDescription {@link ExplicitSegmentDescription} to visit
     */
    public void visit(ExplicitSegmentDescription segmentDescription);

    /**
     * Visit a {@link NoSegmentationSegmentDescription}
     *
     * @param segmentDescription {@link NoSegmentationSegmentDescription} to visit
     */
    public void visit(NoSegmentationSegmentDescription segmentDescription);
}
