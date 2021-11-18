package com.exasol.adapter.document.documentfetcher.files.segmentation;

import lombok.Getter;

/**
 * This class builds {@link SegmentMatcher}s for given {@link SegmentDescription}s.
 */
public class SegmentMatcherFactory {

    private SegmentMatcherFactory() {
        // Static class; no constructor needed.
    }

    /**
     * Build a {@link SegmentMatcher} for a given {@link SegmentDescription}.
     *
     * @param segmentDescription {@link SegmentDescription} to build a matcher for
     * @return built {@link SegmentMatcher}
     */
    public static SegmentMatcher buildSegmentMatcher(final SegmentDescription segmentDescription) {
        final MatcherBuildingVisitor visitor = new MatcherBuildingVisitor();
        segmentDescription.accept(visitor);
        return visitor.getSegmentMatcher();
    }

    private static class MatcherBuildingVisitor implements SegmentDescriptionVisitor {
        @Getter
        private SegmentMatcher segmentMatcher;

        @Override
        public void visit(final HashSegmentDescription segmentDescription) {
            this.segmentMatcher = new HashSegmentMatcher(segmentDescription);
        }

        @Override
        public void visit(final ExplicitSegmentDescription segmentDescription) {
            this.segmentMatcher = new ExplicitSegmentMatcher(segmentDescription);
        }

        @Override
        public void visit(final NoSegmentationSegmentDescription segmentDescription) {
            this.segmentMatcher = new NoSegmentationSegmentMatcher();
        }
    }
}
