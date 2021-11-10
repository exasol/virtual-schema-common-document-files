package com.exasol.adapter.document.documentfetcher.files;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

/**
 * {@link FileTypeSpecificDocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 3335427484506659234L;
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @Override
    public Iterator<DocumentNode> readDocuments(final RemoteFile remoteFile) {
        try (final JsonReader jsonReader = buildJsonReader(remoteFile.getInputStream())) {
            final JsonValue jsonValue = jsonReader.readValue();
            return List.of(JsonNodeFactory.getInstance().getJsonNode(jsonValue)).iterator();
        } catch (final JsonException jsonException) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-1").message("Error in input file {{JSON_FILE}}.")
                            .parameter("JSON_FILE", remoteFile.getResourceName()).toString(),
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
}
