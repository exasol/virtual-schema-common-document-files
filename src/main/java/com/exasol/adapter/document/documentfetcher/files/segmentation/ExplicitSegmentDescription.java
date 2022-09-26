package com.exasol.adapter.document.documentfetcher.files.segmentation;

import java.util.*;

/**
 * This {@link SegmentDescription} describes a segment by an explicit list of files.
 */
public class ExplicitSegmentDescription implements SegmentDescription {
    private static final long serialVersionUID = -9092990439459054784L;
    /** @serial */
    private final HashMap<String, ArrayList<FileSegmentDescription>> segmentKeys;

    /**
     * Create a new instance of {@link SegmentDescription}.
     * 
     * @param filesToMatch list of files to match
     */
    public ExplicitSegmentDescription(final List<FileSegment> filesToMatch) {
        this.segmentKeys = new HashMap<>();
        for (final FileSegment segmentToMatch : filesToMatch) {
            final String key = segmentToMatch.getFile().getResourceName();
            this.segmentKeys.putIfAbsent(key, new ArrayList<>());
            this.segmentKeys.get(key).add(segmentToMatch.getSegmentDescription());
        }
    }

    /**
     * Get the segment keys.
     * 
     * @return segment keys
     */
    public Map<String, ArrayList<FileSegmentDescription>> getSegmentKeys() {
        return segmentKeys;
    }

    @Override
    public void accept(final SegmentDescriptionVisitor visitor) {
        visitor.visit(this);
    }
}
