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

/**
 * {@link DocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher extends AbstractFilesDocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = 248574935175405437L;
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
            throw new InputDataException("E-VSDF-1 Error in input file '" + loadedFile.getResourceName() + "': "
                    + jsonException.getMessage(), jsonException);
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
