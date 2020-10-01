package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

/**
 * {@link DocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher extends AbstractFilesDocumentFetcher<JsonNodeVisitor> {
    private static final long serialVersionUID = 9127449898816112421L;//

    /**
     * Create an instance of {@link JsonDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    public JsonDocumentFetcher(final String filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        super(filePattern, segmentDescription, fileLoaderFactory);
    }

    @Override
    protected Stream<DocumentNode<JsonNodeVisitor>> readDocuments(final InputStreamWithResourceName loadedFile) {
        try (final JsonReader jsonReader = Json.createReader(loadedFile.getInputStream())) {
            final JsonValue jsonValue = jsonReader.readValue();
            tryToClose(loadedFile);
            return Stream.of(JsonNodeFactory.getInstance().getJsonNode(jsonValue));
        } catch (final JsonException jsonException) {
            throw new InputDataException("E-VSDF-1 Error in input file '" + loadedFile.getResourceName() + "': "
                    + jsonException.getMessage(), jsonException);
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
