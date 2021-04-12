package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;

import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This class provides a {@link JsonLinesIterator} for a given JSON-Lines file.
 */
class JsonLinesIterable implements Iterable<DocumentNode> {
    private final LoadedFile jsonlFile;

    /**
     * Create a new instance of {@link JsonLinesIterable}.
     * 
     * @param jsonlFile JSON lines file
     */
    JsonLinesIterable(final LoadedFile jsonlFile) {
        this.jsonlFile = jsonlFile;
    }

    @Override
    public Iterator<DocumentNode> iterator() {
        return new JsonLinesIterator(this.jsonlFile);
    }
}
