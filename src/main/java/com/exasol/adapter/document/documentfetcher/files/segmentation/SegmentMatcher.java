package com.exasol.adapter.document.documentfetcher.files.segmentation;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * Interface for classes that separates a set of files into multiple segments.
 */
public interface SegmentMatcher {
    /**
     * Matches file names that belong to the {@link HashSegmentDescription} passed to the constructor.
     *
     * @param loadedFile file to match
     * @return {@code true} if file belongs to this partition
     */
    boolean matches(RemoteFile loadedFile);
}
