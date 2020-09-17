package com.exasol.adapter.document.documentfetcher.files;

/**
 * This class separates a set of files into multiple segments.
 * 
 * <p>
 * This matcher is used on different parallel runs to partition the files. It uses a hash of the file name to determine
 * the partition of the file. Due to that implementation the segments are not guaranteed to have exactly the same size.
 * The reason for this implementation is, that it works independent of the order in that the files are matched.
 * </p>
 */
class SegmentMatcher {
    private final SegmentDescription segmentDescription;

    /**
     * Create a new instance of {@link SegmentDescription}.
     * 
     * @param segmentDescription segment description
     */
    public SegmentMatcher(final SegmentDescription segmentDescription) {
        this.segmentDescription = segmentDescription;
    }

    /**
     * Matches file names that belong to the {@link SegmentDescription} passed to the constructor.
     * 
     * @param fileName file name to match
     * @return {@code true} if file belongs to this partition
     */
    public boolean matches(final String fileName) {
        @java.lang.SuppressWarnings("squid:S2676") // abs hashcode is intended here
        final long hashNumber = Math.abs(fileName.hashCode());
        final int modulo = (int) (hashNumber % this.segmentDescription.getNumSegments());
        return modulo == this.segmentDescription.getSegmentId();
    }
}
