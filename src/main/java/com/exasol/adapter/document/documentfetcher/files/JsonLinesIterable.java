package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

/**
 * This class provides a {@link JsonLinesIterator} for a given JSON-Lines file.
 */
class JsonLinesIterable implements Iterable<DocumentNode<JsonNodeVisitor>> {
    private final FileLoader fileLoader;

    JsonLinesIterable(final FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    @Override
    public Iterator<DocumentNode<JsonNodeVisitor>> iterator() {
        return new JsonLinesIterator(this.fileLoader);
    }
}
