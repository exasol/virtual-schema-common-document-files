package com.exasol.adapter.document.documentfetcher.files;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * {@link DocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher extends AbstractFilesDocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = -4079602566145893845L;

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
    protected Stream<DocumentNode<JsonNodeVisitor>> readDocuments(final InputStreamWithResourceName loadedFile) {
        return StreamSupport.stream(new JsonLinesIterable(loadedFile).spliterator(), false);
    }
}
