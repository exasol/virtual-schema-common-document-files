package com.exasol.adapter.document.documentfetcher.files.segmentation;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

/**
 * This {@link SegmentMatcher} accepts all files since no segmentation is needed.
 */
public class NoSegmentationSegmentMatcher implements SegmentMatcher {
    @Override
    public boolean matches(final RemoteFile remoteFile) {
        return true;
    }
}
