package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

/**
 * {@link DocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher implements DocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = -8855870887951143439L;
    private final String filePattern;
    SegmentDescription segmentDescription;
    private final FileLoaderFactory fileLoaderFactory;

    public static final String FILE_EXTENSION = ".json";

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    public JsonDocumentFetcher(final String filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        this.filePattern = filePattern;
        this.segmentDescription = segmentDescription;
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public Stream<DocumentNode<JsonNodeVisitor>> run(final ExaConnectionInformation connectionInformation) {
        final Stream<InputStream> jsonStream = this.fileLoaderFactory
                .getLoader(this.filePattern, this.segmentDescription, connectionInformation).loadFiles();
        return jsonStream.map(this::readJson);
    }

    private DocumentNode<JsonNodeVisitor> readJson(final InputStream jsonStream) {
        try (final JsonReader jsonReader = Json.createReader(jsonStream)) {
            final JsonValue jsonValue = jsonReader.readValue();
            try {
                jsonStream.close();
            } catch (final IOException exception) {
                // ignore
            }
            return JsonNodeFactory.getInstance().getJsonNode(jsonValue);
        }
    }
}
