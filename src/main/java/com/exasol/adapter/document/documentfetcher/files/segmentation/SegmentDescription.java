package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.io.Serializable;

/**
 * Interface for classes that describe a subset of the files to load.
 */
public interface SegmentDescription extends Serializable {

    /**
     * Accept a {@link SegmentDescriptionVisitor}.
     * 
     * @param visitor visitor to accept
     */
    public void accept(SegmentDescriptionVisitor visitor);
}
