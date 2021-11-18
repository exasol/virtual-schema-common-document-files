package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.HashSet;
import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import lombok.Getter;

/**
 * This {@link SegmentDescription} describes a segment by an explicit list of files.
 */
public class ExplicitSegmentDescription implements SegmentDescription {
    private static final long serialVersionUID = 2081718935447634522L;
    /** @serial */
    @Getter
    private final HashSet<String> segmentKeys;

    /**
     * Create a new instance of {@link SegmentDescription}.
     * 
     * @param filesToMatch list of files to match
     */
    public ExplicitSegmentDescription(final List<RemoteFile> filesToMatch) {
        this.segmentKeys = new HashSet<>();
        for (final RemoteFile fileToMatch : filesToMatch) {
            this.segmentKeys.add(fileToMatch.getResourceName());
        }
    }

    @Override
    public void accept(final SegmentDescriptionVisitor visitor) {
        visitor.visit(this);
    }
}
