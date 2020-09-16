package com.exasol.adapter.document.documentfetcher.files;

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
    private final String fileName;

    public static final String FILE_EXTENSION = ".json";

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     * 
     * @param fileName file pattern
     */
    public JsonDocumentFetcher(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Stream<DocumentNode<JsonNodeVisitor>> run(final ExaConnectionInformation connectionInformation) {
        final InputStream jsonStream = FileLoaderFactory.getInstance().getLoader(this.fileName, connectionInformation)
                .loadFile();
        try (final JsonReader jsonReader = Json.createReader(jsonStream)) {
            final JsonValue jsonValue = jsonReader.readValue();
            return Stream.of(JsonNodeFactory.getInstance().getJsonNode(jsonValue));
        }
    }
}
