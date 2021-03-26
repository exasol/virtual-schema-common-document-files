package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;

class JsonLinesIteratorTest {
    public static final String JSON_LINES_EXAMPLE = "{\"id\": \"test-1\"}\n{\"id\": \"test-2\"}";

    @Test
    void testReadLines() {
        final List<DocumentNode<JsonNodeVisitor>> result = readJsonLines(JSON_LINES_EXAMPLE);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testReadLinesWithAdditionalNewLine() {
        final List<DocumentNode<JsonNodeVisitor>> result = readJsonLines(JSON_LINES_EXAMPLE + "\n\n");
        assertThat(result.size(), equalTo(2));
    }

    @Test
    void testHasNextHasNoSideEffects() {
        final JsonLinesIterator jsonLinesIterator = getJsonLinesIterator(JSON_LINES_EXAMPLE);
        jsonLinesIterator.hasNext();
        jsonLinesIterator.hasNext();
        jsonLinesIterator.next();
        jsonLinesIterator.next();
        assertThat(jsonLinesIterator.hasNext(), equalTo(false));
    }

    @Test
    void testSyntaxError() {
        final InputDataException exception = assertThrows(InputDataException.class,
                () -> readJsonLines("{}\n{notQutes :: - \"wrong syntax}"));
        assertThat(exception.getMessage(),
                startsWith("E-VSDF-3: Failed to parse JSON-Lines from 'string'. Invalid JSON document in line 2."));
    }

    @Test
    void testFinalize() throws Throwable {
        final CloseCheckLoadedFile closeCheckLoadedFile = new CloseCheckLoadedFile("");
        final JsonLinesIterator jsonLinesIterator = new JsonLinesIterator(closeCheckLoadedFile);
        jsonLinesIterator.finalize();
        assertThat(closeCheckLoadedFile.wasStreamClosed(), equalTo(true));
    }

    @Test
    void testNoSuchElementException() {
        final JsonLinesIterator jsonLinesIterator = getJsonLinesIterator(JSON_LINES_EXAMPLE);
        jsonLinesIterator.next();
        jsonLinesIterator.next();
        assertThrows(NoSuchElementException.class, jsonLinesIterator::next);
    }

    private List<DocumentNode<JsonNodeVisitor>> readJsonLines(final String s) {
        final JsonLinesIterator jsonLinesIterator = getJsonLinesIterator(s);
        final List<DocumentNode<JsonNodeVisitor>> result = new ArrayList<>();
        jsonLinesIterator.forEachRemaining(result::add);
        return result;
    }

    private JsonLinesIterator getJsonLinesIterator(final String content) {
        return new JsonLinesIterator(new StringLoadedFile(content, "string"));
    }
}