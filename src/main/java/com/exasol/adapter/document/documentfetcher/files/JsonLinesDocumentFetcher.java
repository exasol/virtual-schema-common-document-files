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
    private final String fileName;
    public static final String FILE_EXTENSION = ".jsonl";

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     *
     * @param fileName JSON-lines file name
     */
    public JsonLinesDocumentFetcher(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Stream<DocumentNode<JsonNodeVisitor>> run(final ExaConnectionInformation connectionInformation) {
        final FileLoader fileLoader = FileLoaderFactory.getInstance().getLoader(this.fileName, connectionInformation);
        return StreamSupport.stream(new JsonLinesIterable(fileLoader).spliterator(), false);
    }

}
