package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import javax.json.*;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.errorreporting.ExaError;

/**
 * {@link DocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher extends AbstractFilesDocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = 5377870560856410930L;
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    public JsonDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        super(filePattern, segmentDescription, fileLoaderFactory);
    }

    @Override
    protected Stream<DocumentNode<JsonNodeVisitor>> readDocuments(final InputStreamWithResourceName loadedFile) {
        try (final JsonReader jsonReader = buildJsonReader(loadedFile.getInputStream())) {
            final JsonValue jsonValue = jsonReader.readValue();
            tryToClose(loadedFile);
            return Stream.of(JsonNodeFactory.getInstance().getJsonNode(jsonValue));
        } catch (final JsonException jsonException) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-1").message("Error in input file {{JSON_FILE}}.")
                            .parameter("JSON_FILE", loadedFile.getResourceName()).toString(),
                    jsonException);
        }
    }

    private JsonReader buildJsonReader(final InputStream inputStream) {
        try {
            return JSON_READER_FACTORY.createReader(inputStream);
        } catch (final JsonException exception) {
            if (exception.getMessage().equals("Cannot auto-detect encoding, not enough chars")) {
                return JSON_READER_FACTORY.createReader(inputStream, java.nio.charset.StandardCharsets.UTF_8);
            } else {
                throw exception;
            }
        }
    }

    private void tryToClose(final InputStreamWithResourceName loadedFile) {
        try {
            loadedFile.close();
        } catch (final IOException exception) {
            // ignore
        }
    }
}
