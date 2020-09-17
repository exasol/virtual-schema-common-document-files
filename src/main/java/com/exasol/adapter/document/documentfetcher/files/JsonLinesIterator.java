package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
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
    private final InputStream jsonlStream;
    private final String fileName;
    private final InputStreamReader inputStreamReader;
    private String nextLine = null;
    private long lineCounter = 0;

    /**
     * Create a new instance of {@link JsonLinesIterator}.
     * 
     * @param fileLoader file loader for the JSON-Lines file
     */
    JsonLinesIterator(final FileLoader fileLoader) {
        this.jsonlStream = fileLoader.loadFiles().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find data file."));
        this.fileName = fileLoader.getFilePattern();
        this.inputStreamReader = new InputStreamReader(this.jsonlStream);
        this.jsonlReader = new BufferedReader(this.inputStreamReader);
        readNextLine();
    }

    @Override
    protected void finalize() throws Throwable {
        this.jsonlStream.close();
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
            throw new IllegalStateException("Failed to read data. Cause: " + exception.getMessage());
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
                throw new IllegalArgumentException("Failed to parse JSON-Lines from " + this.fileName
                        + ". Invalid JSON document in line " + this.lineCounter + ". " + exception.getMessage(),
                        exception);
            }
        }
    }
}
