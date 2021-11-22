package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.io.Serializable;

import lombok.Data;

/**
 * This class describes a segment of a file.
 */
@Data
public class FileSegmentDescription implements Serializable {
    /** Constant for {@link FileSegmentDescription} of the entire file. */
    public static final FileSegmentDescription ENTIRE_FILE = new FileSegmentDescription(1, 0);
    private static final long serialVersionUID = 8989312140293872959L;
    /**
     * Total number of segments
     * 
     * @serial
     */
    private final int numberOfSegments;
    /**
     * ID of this segment
     * 
     * @serial
     */
    private final int segmentId;
}
