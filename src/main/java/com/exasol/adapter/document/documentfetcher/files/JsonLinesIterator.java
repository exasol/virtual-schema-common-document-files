package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.json.*;
import javax.json.spi.JsonProvider;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.errorreporting.ExaError;

/**
 * This class iterates the lines of a JSON-Lines file an creates for each line a JSON {@link DocumentNode}.
 */
class JsonLinesIterator implements Iterator<DocumentNode> {
    private static final JsonProvider JSON = JsonProvider.provider();
    private final BufferedReader jsonlReader;
    private final InputStreamReader inputStreamReader;
    private String nextLine = null;
    private long lineCounter = 0;
    private final String resourceName;

    /**
     * Create a new instance of {@link JsonLinesIterator}.
     * 
     * @param jsonlFile file loader for the JSON-Lines file
     */
    JsonLinesIterator(final LoadedFile jsonlFile) {
        this.inputStreamReader = new InputStreamReader(jsonlFile.getInputStream());
        this.jsonlReader = new BufferedReader(this.inputStreamReader);
        this.resourceName = jsonlFile.getResourceName();
        readNextLine();
    }

    private void closeResources() {
        try {
            this.inputStreamReader.close();
            this.jsonlReader.close();
        } catch (final IOException exception) {
            // at least we tried...
        }
    }

    private void readNextLine() {
        try {
            do {
                this.nextLine = this.jsonlReader.readLine();
                this.lineCounter++;
            } while (this.nextLine != null && this.nextLine.isBlank());
            if (!hasNext()) {
                closeResources();
            }
        } catch (final IOException exception) {
            throw new InputDataException(
                    ExaError.messageBuilder("E-VSDF-2").message("Failed to read from data file {{JSONL_FILE}}.")
                            .parameter("JSONL_FILE", this.resourceName).toString(),
                    exception);
        }
    }

    @Override
    public boolean hasNext() {
        return this.nextLine != null;
    }

    @Override
    public DocumentNode next() {
        if (this.nextLine == null) {
            throw new NoSuchElementException();
        }
        try (final JsonReader jsonReader = JSON.createReader(new StringReader(this.nextLine))) {
            try {
                final JsonValue jsonValue = jsonReader.readValue();
                readNextLine();
                return JsonNodeFactory.getInstance().getJsonNode(jsonValue);
            } catch (final JsonException exception) {
                throw new InputDataException(ExaError.messageBuilder("E-VSDF-3").message(
                        "Failed to parse JSON-Lines from {{JSONL_FILE}}. Invalid JSON document in line {{LINE}}.")
                        .parameter("JSONL_FILE", this.resourceName).parameter("LINE", this.lineCounter).toString(),
                        exception);
            }
        }
    }
}
