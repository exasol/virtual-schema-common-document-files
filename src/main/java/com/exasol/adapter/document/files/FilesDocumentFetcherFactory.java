package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.CloseableIterator;

/**
 * Factory for {@link FilesDocumentFetcher}.
 */
public class FilesDocumentFetcherFactory {
    private static final int MAX_NUMBER_OF_FILES_TO_DISTRIBUTE_EXPLICITLY = 200;
    private static final int ONE_MB = 1_000_000;
    private static final int MIN_SIZE_PER_WORKER = ONE_MB;

    /**
     * Builds {@link DocumentFetcher}s for a given query.
     *
     * @param sourceFilter                    filter for the source file names
     * @param maxNumberOfParallelFetchers     the maximum amount of {@link DocumentFetcher}s that can be used in
     *                                        parallel
     * @param fileFinderFactory               dependency injection of {@link FileFinderFactory}
     * @param connectionInformation           connection information
     * @param fileTypeSpecificDocumentFetcher file type specific document fetcher
     * @return built {@link DocumentFetcher}
     */
    public List<DocumentFetcher> buildDocumentFetcherForQuery(final StringFilter sourceFilter,
                                                              final int maxNumberOfParallelFetchers, final FileFinderFactory fileFinderFactory,
                                                              final ConnectionPropertiesReader connectionInformation,
                                                              final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher) {
        final List<DocumentFetcher> documentFetchers = new ArrayList<>(maxNumberOfParallelFetchers);
        final int numberOfSegments = sourceFilter.hasWildcard() ? maxNumberOfParallelFetchers : 1;
        final List<SegmentDescription> segmentDescriptions = buildSegmentDescriptions(fileFinderFactory,
                connectionInformation, numberOfSegments, sourceFilter, fileTypeSpecificDocumentFetcher);
        for (final SegmentDescription segmentDescription : segmentDescriptions) {
            final DocumentFetcher documentFetcher = new FilesDocumentFetcher(sourceFilter, segmentDescription,
                    fileFinderFactory, fileTypeSpecificDocumentFetcher);
            documentFetchers.add(documentFetcher);
        }
        return documentFetchers;
    }

    private List<SegmentDescription> buildSegmentDescriptions(final FileFinderFactory fileFinderFactory,
                                                              final ConnectionPropertiesReader connectionInformation, final int numberOfSegments,
                                                              final StringFilter filePattern, final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher) {
        final RemoteFileFinder loader = fileFinderFactory.getFinder(filePattern, connectionInformation);
        try (final CloseableIterator<RemoteFile> iterator = loader.loadFiles()) {
            final List<RemoteFile> firstFiles = new ArrayList<>();
            int remoteFileCounter = 0;
            while (iterator.hasNext() && remoteFileCounter < MAX_NUMBER_OF_FILES_TO_DISTRIBUTE_EXPLICITLY) {
                firstFiles.add(iterator.next());
                remoteFileCounter++;
            }
            if (iterator.hasNext()) {
                return buildHashSegmentation(numberOfSegments);
            } else {
                return buildExplicitSegmentation(numberOfSegments, firstFiles,
                        fileTypeSpecificDocumentFetcher.supportsFileSplitting());
            }
        }
    }

    private List<SegmentDescription> buildExplicitSegmentation(final int numberOfSegments, final List<RemoteFile> files,
                                                               final boolean fileSplittingIsSupported) {
        final int numberOfWorkers = limitWorkerCountByFileSize(numberOfSegments, files);
        final List<FileSegment> splitFiles = splitFilesIfRequired(numberOfWorkers, files, fileSplittingIsSupported);
        final List<FileSegment>[] bins = new BinDistributor().distributeInBins(splitFiles, numberOfWorkers);
        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfWorkers);
        for (final List<FileSegment> bin : bins) {
            if (!bin.isEmpty()) {
                segmentDescriptions.add(new ExplicitSegmentDescription(bin));
            }
        }
        return segmentDescriptions;
    }

    private int limitWorkerCountByFileSize(final int numberOfSegments, final List<RemoteFile> files) {
        final long sum = getTotalSize(files);
        final int calculatedWorkerCount = (int) (sum / MIN_SIZE_PER_WORKER);
        if (calculatedWorkerCount < 1) {
            return 1;
        }
        return Math.min(numberOfSegments, calculatedWorkerCount);
    }

    private long getTotalSize(final List<RemoteFile> files) {
        long sum = 0;
        for (final RemoteFile file : files) {
            sum += file.getSize();
        }
        return sum;
    }

    private List<FileSegment> splitFilesIfRequired(final int numberOfSegments, final List<RemoteFile> files,
                                                   final boolean fileSplittingIsSupported) {
        final List<FileSegment> splitFiles = new ArrayList<>();
        if (files.size() < numberOfSegments && fileSplittingIsSupported) {
            final int factor = (int) Math.ceil((double) numberOfSegments / files.size());
            for (final RemoteFile file : files) {
                if (isFileWorthSplitting(file)) {
                    for (int splitCounter = 0; splitCounter < factor; splitCounter++) {
                        splitFiles.add(new FileSegment(file, new FileSegmentDescription(factor, splitCounter)));
                    }
                } else {
                    splitFiles.add(new FileSegment(file, ENTIRE_FILE));
                }
            }
        } else {
            for (final RemoteFile file : files) {
                splitFiles.add(new FileSegment(file, ENTIRE_FILE));
            }
        }
        return splitFiles;
    }

    private boolean isFileWorthSplitting(final RemoteFile file) {
        return file.getSize() > ONE_MB;
    }

    private List<SegmentDescription> buildHashSegmentation(final int numberOfSegments) {
        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfSegments);
        for (int segmentCounter = 0; segmentCounter < numberOfSegments; segmentCounter++) {
            segmentDescriptions.add(new HashSegmentDescription(numberOfSegments, segmentCounter));
        }
        return segmentDescriptions;
    }
}
