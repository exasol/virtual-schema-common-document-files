package com.exasol.adapter.document.files;

import java.util.*;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Factory for {@link FilesDocumentFetcher}.
 */
public class FilesDocumentFetcherFactory {
    private static final int MAX_NUMBER_OF_FILES_TO_DISTRIBUTE_EXPLICITLY = 200;

    /**
     * Builds {@link DocumentFetcher}s for a given query.
     *
     * @param sourceFilter                    filter for the source file names
     * @param maxNumberOfParallelFetchers     the maximum amount of {@link DocumentFetcher}s that can be used in
     *                                        parallel
     * @param fileLoaderFactory               dependency injection of {@link FileLoaderFactory}
     * @param connectionInformation           connection information
     * @param fileTypeSpecificDocumentFetcher file type specific document fetcher
     * @return built {@link DocumentFetcher}
     */
    public List<DocumentFetcher> buildDocumentFetcherForQuery(final StringFilter sourceFilter,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory,
            final ExaConnectionInformation connectionInformation,
            final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher) {
        final List<DocumentFetcher> documentFetchers = new ArrayList<>(maxNumberOfParallelFetchers);
        final int numberOfSegments = sourceFilter.hasWildcard() ? maxNumberOfParallelFetchers : 1;
        final StringFilter filterWithPreventedInjection = addPrefixToFilterToAvoidInjection(sourceFilter,
                connectionInformation);
        final List<SegmentDescription> segmentDescriptions = buildSegmentDescriptions(fileLoaderFactory,
                connectionInformation, numberOfSegments, filterWithPreventedInjection);
        for (final SegmentDescription segmentDescription : segmentDescriptions) {
            final DocumentFetcher documentFetcher = new FilesDocumentFetcher(filterWithPreventedInjection,
                    segmentDescription, fileLoaderFactory, fileTypeSpecificDocumentFetcher);
            documentFetchers.add(documentFetcher);
        }
        return documentFetchers;
    }

    private StringFilter addPrefixToFilterToAvoidInjection(final StringFilter sourceFilter,
            final ExaConnectionInformation connectionInformation) {
        final String prefix = connectionInformation.getAddress();
        final StringFilter filePatternWithPrefix = new PrefixPrepender().prependStaticPrefix(prefix, sourceFilter);
        return new StringFilterFactory().and(filePatternWithPrefix, WildcardExpression.forNonWildcardPrefix(prefix));
    }

    private List<SegmentDescription> buildSegmentDescriptions(final FileLoaderFactory fileLoaderFactory,
            final ExaConnectionInformation connectionInformation, final int numberOfSegments,
            final StringFilter filePattern) {
        final FileLoader loader = fileLoaderFactory.getLoader(filePattern, new NoSegmentationSegmentDescription(),
                connectionInformation);
        final Iterator<RemoteFile> iterator = loader.loadFiles();
        final List<RemoteFile> firstFiles = new ArrayList<>();
        int remoteFileCounter = 0;
        while (iterator.hasNext() && remoteFileCounter < MAX_NUMBER_OF_FILES_TO_DISTRIBUTE_EXPLICITLY) {
            firstFiles.add(iterator.next());
            remoteFileCounter++;
        }
        if (iterator.hasNext()) {
            return buildHashSegmentation(numberOfSegments);
        } else {
            return buildExplicitSegmentation(numberOfSegments, firstFiles);
        }
    }

    private List<SegmentDescription> buildExplicitSegmentation(final int numberOfSegments,
            final List<RemoteFile> firstFiles) {
        final List<RemoteFile>[] bins = distributeInEqualySizedBins(firstFiles, numberOfSegments);
        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfSegments);
        for (final List<RemoteFile> bin : bins) {
            segmentDescriptions.add(new ExplicitSegmentDescription(bin));
        }
        return segmentDescriptions;
    }

    private List<RemoteFile>[] distributeInEqualySizedBins(final List<RemoteFile> firstFiles, final int numberOfBins) {
        final List<RemoteFile>[] bins = new List[numberOfBins];
        for (int index = 0; index < numberOfBins; index++) {
            bins[index] = new ArrayList<>();
        }
        int counter = 0;
        for (final RemoteFile file : firstFiles) {
            bins[counter % numberOfBins].add(file);
            counter++;
        }
        return bins;
    }

    private List<SegmentDescription> buildHashSegmentation(final int numberOfSegments) {
        final List<SegmentDescription> segmentDescriptions = new ArrayList<>(numberOfSegments);
        for (int segmentCounter = 0; segmentCounter < numberOfSegments; segmentCounter++) {
            segmentDescriptions.add(new HashSegmentDescription(numberOfSegments, segmentCounter));
        }
        return segmentDescriptions;
    }
}
