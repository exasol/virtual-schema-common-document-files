package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.*;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * Matcher for {@link ExplicitSegmentDescription}s.
 */
public class ExplicitSegmentMatcher implements SegmentMatcher {
    private final Map<String, List<FileSegmentDescription>> segmentKeys;

    /**
     * Create a new instance of {@link ExplicitSegmentMatcher}.
     * 
     * @param segmentDescription segment description
     */
    public ExplicitSegmentMatcher(final ExplicitSegmentDescription segmentDescription) {
        this(segmentDescription.getSegmentKeys());
    }

    /**
     * Create a new instance of {@link ExplicitSegmentMatcher}.
     * 
     * @param segmentKeys segment keys
     */
    public ExplicitSegmentMatcher(final Map<String, List<FileSegmentDescription>> segmentKeys) {
        this.segmentKeys = segmentKeys;
    }

    @Override
    public List<FileSegment> getMatchingSegmentsFor(final RemoteFile remoteFile) {
        final List<FileSegmentDescription> segments = this.segmentKeys.get(remoteFile.getResourceName());
        if (segments == null) {
            return Collections.emptyList();
        } else {
            final List<FileSegment> result = new ArrayList<>(segments.size());
            for (final FileSegmentDescription segment : segments) {
                result.add(new FileSegment(remoteFile, segment));
            }
            return result;
        }
    }

    @Override
    public boolean matchesFile(final RemoteFile remoteFile) {
        final List<FileSegmentDescription> segments = this.segmentKeys.get(remoteFile.getResourceName());
        return segments != null && !segments.isEmpty();
    }
}
