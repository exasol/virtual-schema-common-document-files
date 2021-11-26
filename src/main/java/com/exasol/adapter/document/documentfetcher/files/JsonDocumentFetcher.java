package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;
import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

/**
 * {@link FileTypeSpecificDocumentFetcher} for JSON files.
 */
public class JsonDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = -1587476005325720732L;
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @Override
    public CloseableIterator<DocumentNode> readDocuments(final FileSegment segment) {
        if (!segment.getSegmentDescription().equals(FileSegmentDescription.ENTIRE_FILE)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-16")
                    .message("The JsonDocumentFetcher does not support loading split files.").ticketMitigation()
                    .toString());
        }
        final RemoteFile remoteFile = segment.getFile();
        try (final InputStream inputStream = remoteFile.getInputStream();
                final JsonReader jsonReader = buildJsonReader(inputStream)) {
            final JsonValue jsonValue = jsonReader.readValue();
            final DocumentNode jsonNode = JsonNodeFactory.getInstance().getJsonNode(jsonValue);
            return new CloseableIteratorWrapper<>(List.of(jsonNode).iterator());
        } catch (final JsonException | IOException jsonException) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-1").message("Error in input file {{JSON_FILE}}.")
                            .parameter("JSON_FILE", remoteFile.getResourceName()).toString(),
                    jsonException);
        }
    }

    @Override
    public boolean supportsFileSplitting() {
        return false;
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
