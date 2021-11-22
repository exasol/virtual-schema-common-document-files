package com.exasol.adapter.document.documentfetcher.files.segmentation;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import lombok.Data;

/**
 * This class describes a segment of a file.
 */
@Data
public class FileSegment {
    private static final long serialVersionUID = 1323376679618127370L;
    private final RemoteFile file;
    private final FileSegmentDescription segmentDescription;
}
