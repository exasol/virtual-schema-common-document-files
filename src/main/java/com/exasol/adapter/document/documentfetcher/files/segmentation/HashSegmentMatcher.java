package com.exasol.adapter.document.documentfetcher.files.segmentation;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * This {@link SegmentMatcher} that segments files based on the hash sum of the filename.
 * 
 * <p>
 * This matcher is used on different parallel runs to partition the files. It uses a hash of the file name to determine
 * the partition of the file. Due to that implementation the segments are not guaranteed to have exactly the same size.
 * The reason for this implementation is, that it works independent of the order in that the files are matched.
 * </p>
 */
public class HashSegmentMatcher implements SegmentMatcher {
    private final HashSegmentDescription segmentDescription;

    /**
     * Create a new instance of {@link HashSegmentDescription}.
     * 
     * @param segmentDescription segment description
     */
    public HashSegmentMatcher(final HashSegmentDescription segmentDescription) {
        this.segmentDescription = segmentDescription;
    }

    @Override
    public boolean matches(final RemoteFile remoteFile) {
        @java.lang.SuppressWarnings("squid:S2676") // abs hashcode is intended here
        final long hashNumber = Math.abs(remoteFile.getResourceName().hashCode());
        final int modulo = (int) (hashNumber % this.segmentDescription.getNumberOfSegments());
        return modulo == this.segmentDescription.getSegmentId();
    }
}
