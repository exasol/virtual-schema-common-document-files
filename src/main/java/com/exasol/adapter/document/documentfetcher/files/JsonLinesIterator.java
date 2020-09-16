package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.json.Json;
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
    private final InputStreamReader inputStreamReader;
    private String nextLine = null;

    JsonLinesIterator(final FileLoader fileLoader) {
        this.jsonlStream = fileLoader.loadFile();
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
            this.nextLine = this.jsonlReader.readLine();
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
            final JsonValue jsonValue = jsonReader.readValue();
            readNextLine();
            return JsonNodeFactory.getInstance().getJsonNode(jsonValue);
        }
    }
}
