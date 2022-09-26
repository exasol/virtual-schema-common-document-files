package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class describes a segment of a file.
 */
public final class FileSegmentDescription implements Serializable {
    /** Constant for {@link FileSegmentDescription} of the entire file. */
    public static final FileSegmentDescription ENTIRE_FILE = new FileSegmentDescription(1, 0);
    private static final long serialVersionUID = 8989312140293872959L;

    /** @serial */
    private final int numberOfSegments;
    /** @serial */
    private final int segmentId;

    /**
     * Create a new {@link FileSegmentDescription}.
     * 
     * @param numberOfSegments total number of segments
     * @param segmentId        ID of this segment
     */
    public FileSegmentDescription(final int numberOfSegments, final int segmentId) {
        this.numberOfSegments = numberOfSegments;
        this.segmentId = segmentId;
    }

    /**
     * Get the total number of segments
     *
     * @return total number of segments
     */
    public int getNumberOfSegments() {
        return this.numberOfSegments;
    }

    /**
     * Get the ID of this segment.
     *
     * @return ID of this segment
     */
    public int getSegmentId() {
        return this.segmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.numberOfSegments, this.segmentId);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileSegmentDescription other = (FileSegmentDescription) obj;
        return (this.numberOfSegments == other.numberOfSegments) && (this.segmentId == other.segmentId);
    }
}
