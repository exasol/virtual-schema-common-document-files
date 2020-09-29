package com.exasol.adapter.document.files;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Abstract basis for {@link FilesDataLoaderFactory}.
 */
public abstract class AbstractFilesDataLoaderFactory implements FilesDataLoaderFactory {
    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory) {
        final List<DataLoader> dataLoaders = new ArrayList<>(maxNumberOfParallelFetchers);
        final String sourceString = remoteTableQuery.getFromTable().getRemoteName();
        final int numberOfSegments = hasGlob(sourceString) ? maxNumberOfParallelFetchers : 1;
        for (int segmentCounter = 0; segmentCounter < numberOfSegments; segmentCounter++) {
            final SegmentDescription segmentDescription = new SegmentDescription(numberOfSegments, segmentCounter);
            final DataLoader dataLoader = buildSingleDataLoader(fileLoaderFactory, segmentDescription, sourceString);
            dataLoaders.add(dataLoader);
        }
        return dataLoaders;
    }

    private boolean hasGlob(final String sourceString) {
        return sourceString.contains("?") || sourceString.contains("*");
    }

    /**
     * Build the document type specific {@link DataLoader}.
     * 
     * @param fileLoaderFactory  dependency injection for {@link FileLoaderFactory}.
     * @param segmentDescription {@link SegmentDescription} for parallelization.
     * @param sourceString       file pattern of the files to load.
     * @return built {@link DataLoader}.
     */
    protected abstract DataLoader buildSingleDataLoader(FileLoaderFactory fileLoaderFactory,
            SegmentDescription segmentDescription, String sourceString);
}
