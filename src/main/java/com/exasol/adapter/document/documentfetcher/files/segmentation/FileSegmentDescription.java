package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileSegmentDescription implements Serializable {
    public static final FileSegmentDescription ENTIRE_FILE = new FileSegmentDescription(1, 0);
    private static final long serialVersionUID = 1323376679618127370L;
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
