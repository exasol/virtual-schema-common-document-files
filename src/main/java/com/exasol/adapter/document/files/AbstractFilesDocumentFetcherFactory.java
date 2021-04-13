package com.exasol.adapter.document.files;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Abstract basis for {@link FilesDocumentFetcherFactory}.
 */
public abstract class AbstractFilesDocumentFetcherFactory implements FilesDocumentFetcherFactory {
    @Override
    public List<DocumentFetcher> buildDocumentFetcherForQuery(final StringFilter sourceFilter,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory) {
        final List<DocumentFetcher> dataLoaders = new ArrayList<>(maxNumberOfParallelFetchers);
        final int numberOfSegments = sourceFilter.hasWildcard() ? maxNumberOfParallelFetchers : 1;
        for (int segmentCounter = 0; segmentCounter < numberOfSegments; segmentCounter++) {
            final SegmentDescription segmentDescription = new SegmentDescription(numberOfSegments, segmentCounter);
            final DocumentFetcher dataLoader = buildSingleDocumentFetcher(fileLoaderFactory, segmentDescription,
                    sourceFilter);
            dataLoaders.add(dataLoader);
        }
        return dataLoaders;
    }

    /**
     * Build the document type specific {@link DocumentFetcher}.
     * 
     * @param fileLoaderFactory  dependency injection for {@link FileLoaderFactory}
     * @param segmentDescription {@link SegmentDescription} for parallelization
     * @param sourceFilter       filter for the source file names
     * @return built {@link DocumentFetcher}.
     */
    protected abstract DocumentFetcher buildSingleDocumentFetcher(FileLoaderFactory fileLoaderFactory,
            SegmentDescription segmentDescription, StringFilter sourceFilter);
}
