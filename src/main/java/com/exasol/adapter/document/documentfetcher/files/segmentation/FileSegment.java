package com.exasol.adapter.document.documentfetcher.files.segmentation;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import lombok.Data;

/**
 * This class describes a segment of a file.
 */
@Data
public class FileSegment {
    private static final long serialVersionUID = -4769959252871575175L;
    private final RemoteFile file;
    private final FileSegmentDescription segmentDescription;

    /**
     * Get an estimated size of this segment.
     * 
     * @return estimated size in bytes
     */
    public long estimateSize() {
        return this.file.getSize() / this.segmentDescription.getNumberOfSegments();
    }
}
