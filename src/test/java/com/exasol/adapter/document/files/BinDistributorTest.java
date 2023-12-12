package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;

class BinDistributorTest {

    @Test
    void testNegativeNumberOfBins() {
        final BinDistributor binDistributor = new BinDistributor();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> binDistributor.distributeInBins(null, -1));
        assertThat(exception.getMessage(), equalTo("F-VSDF-23: Number of bins must be > 0 but is -1"));
    }

    @ParameterizedTest
    @CsvSource({ "30, 10,10,10", "31, 11,10,10", "32, 11,11,10", "2, 1,1,0", "0, 0,0,0" })
    void testDistributeEquallySizedFiles(final int total, final int expectedInBin1, final int expectedInBin2,
            final int expectedInBin3) {
        final List<FileSegment> segments = createEquallySizedSegments(total);
        final List<List<FileSegment>> bins = new BinDistributor().distributeInBins(segments, 3);
        final List<Integer> binSizes = bins.stream().map(List::size).collect(Collectors.toList());
        assertAll(//
                () -> assertThat(bins.size(), Matchers.equalTo(3)),
                () -> assertThat(binSizes, Matchers.containsInAnyOrder(expectedInBin1, expectedInBin2, expectedInBin3)) //
        );
    }

    private List<FileSegment> createEquallySizedSegments(final int amount) {
        final List<FileSegment> segments = new ArrayList<>(amount);
        for (int counter = 0; counter < amount; counter++) {
            segments.add(createSegment(1_000L));
        }
        return segments;
    }

    private FileSegment createSegment(final long size) {
        final RemoteFile remoteFile = mock(RemoteFile.class);
        when(remoteFile.getSize()).thenReturn(size);
        return new FileSegment(remoteFile, ENTIRE_FILE);
    }

    @Test
    void testDistributionWithNonEqualSizes() {
        final List<FileSegment> segments = createDifferentSizedSegments(99);
        Collections.shuffle(segments);
        final List<List<FileSegment>> bins = new BinDistributor().distributeInBins(segments, 2);
        final long bin1Size = getTotalSize(bins.get(0));
        final long bin2Size = getTotalSize(bins.get(1));
        final long totalSize = bin1Size + bin2Size;
        final long maxSize = (long) (totalSize * 0.55);
        assertAll(//
                () -> assertThat(bin1Size, Matchers.lessThan(maxSize)),
                () -> assertThat(bin2Size, Matchers.lessThan(maxSize))//
        );
    }

    private List<FileSegment> createDifferentSizedSegments(final int amount) {
        final List<FileSegment> segments = new ArrayList<>(amount);
        for (int counter = 0; counter < amount; counter++) {
            segments.add(createSegment(10L * counter));
        }
        return segments;
    }

    private long getTotalSize(final List<FileSegment> bin) {
        return bin.stream().mapToLong(FileSegment::estimateSize).sum();
    }
}
