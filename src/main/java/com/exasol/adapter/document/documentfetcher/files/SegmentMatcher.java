package com.exasol.adapter.document.documentfetcher.files;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private final MessageDigest sha256;

    /**
     * Create a new instance of {@link SegmentDescription}.
     * 
     * @param segmentDescription segment description
     */
    public SegmentMatcher(final SegmentDescription segmentDescription) {
        this.segmentDescription = segmentDescription;
        try {
            this.sha256 = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Missing dependency for sha-256. Cause: " + exception.getMessage(),
                    exception);
        }
    }

    /**
     * Matches file names that belong to the {@link SegmentDescription} passed to the constructor.
     * 
     * @param fileName file name to match
     * @return {@code true} if file belongs to this partition
     */
    public boolean matches(final String fileName) {
        final byte[] hash = this.sha256.digest(fileName.getBytes(StandardCharsets.UTF_8));
        final long hashNumber = Math.abs(longFromFirstBytes(hash));
        final int modulo = (int) (hashNumber % this.segmentDescription.getNumSegments());
        return modulo == this.segmentDescription.getSegmentId();
    }

    private long longFromFirstBytes(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, 8);
        buffer.flip();// need flip
        return buffer.getLong();
    }
}
