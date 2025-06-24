package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.FilesDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.CloseableIterator;

/**
 * Factory for {@link FilesDocumentFetcher}.
 */
public class FilesDocumentFetcherFactory {
    private static final Logger LOGGER = Logger.getLogger(FilesDocumentFetcherFactory.class.getName());
    private static final int MAX_NUMBER_OF_FILES_TO_DISTRIBUTE_EXPLICITLY = 200;
    private static final int ONE_MB = 1_000_000;
    private static final int MIN_SIZE_PER_WORKER = ONE_MB;

    /**
     * Builds {@link DocumentFetcher}s for a given query.
     *
     * @param sourceFilter                    filter for the source file names
     * @param maxNumberOfParallelFetchers     maximum amount of {@link DocumentFetcher}s that can be used in parallel
     * @param fileFinderFactory               dependency injection of {@link FileFinderFactory}
     * @param connectionInformation           connection information
     * @param fileTypeSpecificDocumentFetcher file type specific document fetcher
     * @param additionalConfiguration         additional configuration
     * @return built {@link DocumentFetcher}
     */
    public List<DocumentFetcher> buildDocumentFetcherForQuery(final StringFilter sourceFilter,
            final int maxNumberOfParallelFetchers, final FileFinderFactory fileFinderFactory,
            final ConnectionPropertiesReader connectionInformation,
            final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher,
            final String additionalConfiguration) {
        final List<DocumentFetcher> documentFetchers = new ArrayList<>(maxNumberOfParallelFetchers);
        final int numberOfSegments = sourceFilter.hasWildcard() ? maxNumberOfParallelFetchers : 1;
        final List<SegmentDescription> segmentDescriptions = buildSegmentDescriptions(fileFinderFactory,
                connectionInformation, numberOfSegments, sourceFilter, fileTypeSpecificDocumentFetcher);
        if (segmentDescriptions.isEmpty()) {
            LOGGER.fine(() -> getEmptyDocumentFetchersLogMessage(fileFinderFactory,
                    fileTypeSpecificDocumentFetcher, numberOfSegments, sourceFilter, additionalConfiguration));
        }
        for (final SegmentDescription segmentDescription : segmentDescriptions) {
            final DocumentFetcher documentFetcher = new FilesDocumentFetcher(sourceFilter, segmentDescription,
                    fileFinderFactory, fileTypeSpecificDocumentFetcher, additionalConfiguration);
            documentFetchers.add(documentFetcher);
        }
        return documentFetchers;
    }

    /**
     * Generates a detailed log message to be used when no {@link SegmentDescription}s were created
     * during the construction of {@link DocumentFetcher}s.
     * <p>
     * The message includes diagnostic information such as the file pattern, number of segments,
     * a link to the user guide from the {@link FileFinderFactory}, and any additional configuration
     * provided. This is helpful for debugging and understanding why no fetchers were initialized.
     *
     * @param fileFinderFactory      factory providing access to user guide URL
     * @param fileTypeSpecificDocumentFetcher      factory providing access to {@code supportsFileSplitting} flag
     * @param numberOfSegments       number of segments that were attempted to be created
     * @param filePattern            filter pattern used to identify source files
     * @param additionalConfiguration additional configuration that may have influenced the behavior
     * @return a formatted string suitable for logging, explaining why no fetchers were built
     */
    String getEmptyDocumentFetchersLogMessage(final FileFinderFactory fileFinderFactory,
                                              final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher,
                                              final int numberOfSegments,
                                              final StringFilter filePattern,
                                              final String additionalConfiguration) {
        return String.format(
                "No segment descriptions built: " +
                        "[Source filter (File pattern = '%s')], " +
                        "[User guide URL = '%s'], " +
                        "[Number of segments = %d], " +
                        "[Additional config = '%s'], " +
                        "[Document fetcher supports file splitting = '%B']. " +
                        "Returning empty list of DocumentFetcher elements.",
                filePattern,
                fileFinderFactory.getUserGuideUrl(),
                numberOfSegments,
                additionalConfiguration,
                fileTypeSpecificDocumentFetcher.supportsFileSplitting()
        );
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

    private List<SegmentDescription> buildExplicitSegmentation(final int numberOfSegments,
                                                               final List<RemoteFile> files,
                                                               final boolean fileSplittingIsSupported) {
        LOGGER.fine(String.format("Starting explicit segmentation for %d files with %d segments. File splitting supported: %b",
                files.size(), numberOfSegments, fileSplittingIsSupported));

        final int numberOfWorkers = limitWorkerCountByFileSize(numberOfSegments, files);
        LOGGER.fine(String.format("Calculated number of workers (segments): %d", numberOfWorkers));

        final List<FileSegment> splitFiles = splitFilesIfRequired(numberOfWorkers, files, fileSplittingIsSupported);
        LOGGER.fine(String.format("Number of file segments after splitting: %d", splitFiles.size()));

        final List<List<FileSegment>> bins = new BinDistributor().distributeInBins(splitFiles, numberOfWorkers);
        LOGGER.fine(String.format("Distributed file segments into %d bins.", bins.size()));

        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfWorkers);
        int binIndex = 0;
        for (final List<FileSegment> bin : bins) {
            if (!bin.isEmpty()) {
                segmentDescriptions.add(new ExplicitSegmentDescription(bin));
                LOGGER.fine(String.format("Created segment for bin %d with %d file segments.", binIndex, bin.size()));
            } else {
                LOGGER.fine(String.format("Skipped empty bin %d", binIndex));
            }
            binIndex++;
        }

        LOGGER.fine(String.format("Completed building %d explicit segment descriptions.", segmentDescriptions.size()));
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
        LOGGER.fine(() -> "Starting to build hash segmentation for " + numberOfSegments + " segments.");

        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfSegments);
        for (int segmentCounter = 0; segmentCounter < numberOfSegments; segmentCounter++) {
            HashSegmentDescription segment = new HashSegmentDescription(numberOfSegments, segmentCounter);
            segmentDescriptions.add(segment);
            final int counter = segmentCounter;
            LOGGER.fine(() -> String.format("Created hash segment description with counter %d for total %d segments.", counter, numberOfSegments));
        }

        LOGGER.fine("Completed building hash segmentation.");
        return segmentDescriptions;
    }
}
