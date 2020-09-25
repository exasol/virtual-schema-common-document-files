package com.exasol.adapter.document.files;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.JsonDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

/**
 * Factory for JSON {@link JsonDataLoader}.
 */
public class JsonFilesDataLoaderFactory implements FilesDataLoaderFactory {
    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory) {
        final List<DataLoader> dataLoaders = new ArrayList<>(maxNumberOfParallelFetchers);
        for (int segmentCounter = 0; segmentCounter < maxNumberOfParallelFetchers; segmentCounter++) {
            final JsonDocumentFetcher documentFetcher = new JsonDocumentFetcher(
                    remoteTableQuery.getFromTable().getRemoteName(),
                    new SegmentDescription(maxNumberOfParallelFetchers, segmentCounter), fileLoaderFactory);
            dataLoaders.add(new JsonDataLoader(documentFetcher));
        }
        return dataLoaders;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".json");
    }
}
