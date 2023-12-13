package com.exasol.adapter.document.files;

import java.util.*;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.errorreporting.ExaError;

/**
 * This class distributes {@link FileSegment}s into bins of more or less equal size, where size means the sum of the
 * file size of the segments.
 */
class BinDistributor {

    /**
     * Distribute {@link FileSegment}s in bins.
     * 
     * @param segments     segments to distribute
     * @param numberOfBins number of bins
     * @return bins
     */
    List<List<FileSegment>> distributeInBins(final List<FileSegment> segments, final int numberOfBins) {
        if (numberOfBins <= 0) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-23")
                    .message("Number of bins must be > 0 but is {{numberOfBins}}", numberOfBins).toString());
        }
        final ArrayList<FileSegment> segmentsSortedBySize = sortBySizeDesc(segments);
        final Bin[] bins = distribute(segmentsSortedBySize, numberOfBins);
        return getSegments(bins);
    }

    private Bin[] createEmptyBins(final int numberOfBins) {
        final Bin[] bins = new Bin[numberOfBins];
        for (int index = 0; index < numberOfBins; index++) {
            bins[index] = new Bin();
        }
        return bins;
    }

    private Bin[] distribute(final ArrayList<FileSegment> segmentsSortedBySize, final int numberOfBins) {
        final Bin[] bins = createEmptyBins(numberOfBins);
        for (final FileSegment segment : segmentsSortedBySize) {
            findSmallestBin(bins).addSegment(segment);
        }
        return bins;
    }

    private List<List<FileSegment>> getSegments(final Bin[] bins) {
        final List<List<FileSegment>> result = new ArrayList<>(bins.length);
        for (int index = 0; index < bins.length; index++) {
            result.add(bins[index].getSegments());
        }
        return result;
    }

    private ArrayList<FileSegment> sortBySizeDesc(final List<FileSegment> segments) {
        final ArrayList<FileSegment> segmentsSortedBySize = new ArrayList<>(segments);
        final Comparator<FileSegment> comparator = Comparator.comparing(FileSegment::estimateSize);
        segmentsSortedBySize.sort(comparator);
        Collections.reverse(segmentsSortedBySize);
        return segmentsSortedBySize;
    }

    private Bin findSmallestBin(final Bin[] bins) {
        Bin smallest = null;
        long smallestSize = Long.MAX_VALUE;
        for (final Bin bin : bins) {
            if (bin.getSize() < smallestSize) {
                smallest = bin;
                smallestSize = bin.getSize();
            }
        }
        if (smallest == null) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-22")
                    .message("There should always be a bin with size < Long.MAX_VALUE").ticketMitigation().toString());
        }
        return smallest;
    }

    private static class Bin {
        private final List<FileSegment> segments = new ArrayList<>();
        private long size = 0;

        public List<FileSegment> getSegments() {
            return segments;
        }

        public long getSize() {
            return size;
        }

        public void addSegment(final FileSegment segment) {
            this.segments.add(segment);
            this.size += segment.estimateSize();
        }
    }
}
