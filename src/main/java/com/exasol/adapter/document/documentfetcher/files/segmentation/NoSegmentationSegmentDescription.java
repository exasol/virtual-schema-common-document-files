package com.exasol.adapter.document.documentfetcher.files.segmentation;

/**
 * This {@link SegmentDescription} describes no segmentation.
 */
public class NoSegmentationSegmentDescription implements SegmentDescription {
    private static final long serialVersionUID = 633523097342302591L;

    @Override
    public void accept(final SegmentDescriptionVisitor visitor) {
        visitor.visit(this);
    }
}
