package com.exasol.adapter.document.documentfetcher.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

/**
 * This class iterates the lines of a JSON-Lines file an creates for each line a JSON {@link DocumentNode}.
 */
class JsonLinesIterator implements Iterator<DocumentNode<JsonNodeVisitor>> {
    private final BufferedReader jsonlReader;
    private final InputStreamWithResourceName jsonlFile;
    private final InputStreamReader inputStreamReader;
    private String nextLine = null;
    private long lineCounter = 0;

    /**
     * Create a new instance of {@link JsonLinesIterator}.
     * 
     * @param jsonlFile file loader for the JSON-Lines file
     */
    JsonLinesIterator(final InputStreamWithResourceName jsonlFile) {
        this.jsonlFile = jsonlFile;
        this.inputStreamReader = new InputStreamReader(this.jsonlFile.getInputStream());
        this.jsonlReader = new BufferedReader(this.inputStreamReader);
        readNextLine();
    }

    @Override
    protected void finalize() throws Throwable {
        this.jsonlFile.close();
        this.inputStreamReader.close();
        this.jsonlReader.close();
        super.finalize();
    }

    private void readNextLine() {
        try {
            do {
                this.nextLine = this.jsonlReader.readLine();
                this.lineCounter++;
            } while (this.nextLine != null && this.nextLine.isBlank());
        } catch (final IOException exception) {
            throw new InputDataException("E-VSDF-2 Failed to read from data file " + this.jsonlFile.getResourceName()
                    + "'. Cause: " + exception.getMessage(), exception);
        }
    }

    @Override
    public boolean hasNext() {
        return this.nextLine != null;
    }

    @Override
    public DocumentNode<JsonNodeVisitor> next() {
        if (this.nextLine == null) {
            throw new NoSuchElementException();
        }
        try (final JsonReader jsonReader = Json.createReader(new StringReader(this.nextLine))) {
            try {
                final JsonValue jsonValue = jsonReader.readValue();
                readNextLine();
                return JsonNodeFactory.getInstance().getJsonNode(jsonValue);
            } catch (final JsonException exception) {
                throw new InputDataException(
                        "E-VSDF-3 Failed to parse JSON-Lines from " + this.jsonlFile.getResourceName()
                                + ". Invalid JSON document in line " + this.lineCounter + ". " + exception.getMessage(),
                        exception);
            }
        }
    }
}
