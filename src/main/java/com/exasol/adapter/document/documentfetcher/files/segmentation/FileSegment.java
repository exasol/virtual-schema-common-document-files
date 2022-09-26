package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.Objects;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * This class describes a segment of a file.
 */
public final class FileSegment {
    private final RemoteFile file;
    private final FileSegmentDescription segmentDescription;

    /**
     * Create a new {@link FileSegment}.
     *
     * @param file               remote file
     * @param segmentDescription segment description
     */
    public FileSegment(final RemoteFile file, final FileSegmentDescription segmentDescription) {
        this.file = file;
        this.segmentDescription = segmentDescription;
    }

    /**
     * Get the remote file.
     *
     * @return remote file
     */
    public RemoteFile getFile() {
        return this.file;
    }

    /**
     * Get the segment description.
     *
     * @return segment description
     */
    public FileSegmentDescription getSegmentDescription() {
        return this.segmentDescription;
    }

    /**
     * Get an estimated size of this segment.
     *
     * @return estimated size in bytes
     */
    public long estimateSize() {
        return this.file.getSize() / this.segmentDescription.getNumberOfSegments();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.file, this.segmentDescription);
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
        final FileSegment other = (FileSegment) obj;
        return Objects.equals(this.file, other.file)
                && Objects.equals(this.segmentDescription, other.segmentDescription);
    }
}
