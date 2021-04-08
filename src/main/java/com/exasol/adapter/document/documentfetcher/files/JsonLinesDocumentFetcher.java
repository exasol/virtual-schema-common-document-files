package com.exasol.adapter.document.documentfetcher.files;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * {@link DocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher extends AbstractFilesDocumentFetcher {
    private static final long serialVersionUID = -8072558589408147427L;

    /**
     * Create a new instance of {@link JsonLinesDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    public JsonLinesDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        super(filePattern, segmentDescription, fileLoaderFactory);
    }

    @Override
    protected Stream<DocumentNode> readDocuments(final LoadedFile loadedFile) {
        return StreamSupport.stream(new JsonLinesIterable(loadedFile).spliterator(), false);
    }
}
