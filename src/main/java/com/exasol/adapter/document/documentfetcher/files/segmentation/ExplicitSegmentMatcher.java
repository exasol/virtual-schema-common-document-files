package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.*;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import lombok.RequiredArgsConstructor;

/**
 * Matcher for {@link ExplicitSegmentDescription}s.
 */
@RequiredArgsConstructor
public class ExplicitSegmentMatcher implements SegmentMatcher {
    private final HashMap<String, ArrayList<FileSegmentDescription>> segmentKeys;

    /**
     * Create a new instance of {@link ExplicitSegmentMatcher}.
     * 
     * @param segmentDescription segment description
     */
    public ExplicitSegmentMatcher(final ExplicitSegmentDescription segmentDescription) {
        this.segmentKeys = segmentDescription.getSegmentKeys();
    }

    @Override
    public List<FileSegment> getMatchingSegmentsFor(final RemoteFile remoteFile) {
        final ArrayList<FileSegmentDescription> segments = this.segmentKeys.get(remoteFile.getResourceName());
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
        final ArrayList<FileSegmentDescription> segments = this.segmentKeys.get(remoteFile.getResourceName());
        return segments != null && !segments.isEmpty();
    }
}
