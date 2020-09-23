package com.exasol.adapter.document.documentfetcher.files;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

/**
 * {@link DocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher implements DocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = 96660015597997005L;
    private final String fileName;
    private final FileLoaderFactory fileLoaderFactory;
    public static final String FILE_EXTENSION = ".jsonl";

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     *
     * @param fileName          JSON-lines file name
     * @param fileLoaderFactory dependency in injection of {@link FileLoaderFactory}
     */
    public JsonLinesDocumentFetcher(final String fileName, final FileLoaderFactory fileLoaderFactory) {
        this.fileName = fileName;
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public Stream<DocumentNode<JsonNodeVisitor>> run(final ExaConnectionInformation connectionInformation) {
        final FileLoader fileLoader = this.fileLoaderFactory.getLoader(this.fileName,
                SegmentDescription.NO_SEGMENTATION, connectionInformation);
        return StreamSupport.stream(new JsonLinesIterable(fileLoader).spliterator(), false);
    }

}
