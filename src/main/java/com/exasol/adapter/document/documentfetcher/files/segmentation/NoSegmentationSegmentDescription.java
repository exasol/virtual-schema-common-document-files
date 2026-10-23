package com.exasol.adapter.document.documentfetcher.files.segmentation;

/**
 * This {@link SegmentDescription} describes no segmentation.
 */
public class NoSegmentationSegmentDescription implements SegmentDescription {
    private static final long serialVersionUID = 633523097342302591L;

    /**
     * Creates a description that disables segmentation.
     */
    public NoSegmentationSegmentDescription() {
        // Default constructor.
    }

    @Override
    public void accept(final SegmentDescriptionVisitor visitor) {
        visitor.visit(this);
    }
}
