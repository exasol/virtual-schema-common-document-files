package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.HashSet;
import java.util.Set;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import lombok.RequiredArgsConstructor;

/**
 * Matcher for {@link ExplicitSegmentDescription}s.
 */
@RequiredArgsConstructor
public class ExplicitSegmentMatcher implements SegmentMatcher {
    private final Set<String> segmentKeys;

    /**
     * Create a new instance of {@link ExplicitSegmentMatcher}.
     * 
     * @param segmentDescription segment description
     */
    public ExplicitSegmentMatcher(final ExplicitSegmentDescription segmentDescription) {
        this.segmentKeys = new HashSet<>(segmentDescription.getSegmentKeys());
    }

    @Override
    public boolean matches(final RemoteFile remoteFile) {
        return this.segmentKeys.contains(remoteFile.getResourceName());
    }
}
