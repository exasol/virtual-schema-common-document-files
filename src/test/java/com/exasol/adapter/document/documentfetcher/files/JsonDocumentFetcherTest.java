package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

class JsonDocumentFetcherTest {

    @Test
    void testClosed() {
        final AssertStreamIsClosedLoadedFile loadedFile = new AssertStreamIsClosedLoadedFile("{}");
        final FileLoader fileLoader = mock(FileLoader.class);
        when(fileLoader.loadFiles()).thenReturn(Stream.of(loadedFile));
        final FileLoaderFactory loaderFactory = mock(FileLoaderFactory.class);
        when(loaderFactory.getLoader(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(fileLoader);
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher(
                WildcardExpression.forNonWildcardString(""), null, loaderFactory);
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress()).thenReturn("");
        jsonDocumentFetcher.run(connectionInformation).forEach(x -> {
        });
        assertThat(loadedFile.wasStreamClosed(), equalTo(true));
    }

    @Test
    void testReadDocument() {
        final LoadedFile loadedFile = new StringLoadedFile("{\"id\": \"book-1\"}", "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher(null, null, null);
        final List<DocumentNode<JsonNodeVisitor>> result = jsonDocumentFetcher.readDocuments(loadedFile)
                .collect(Collectors.toList());
        assertThat(result.size(), equalTo(1));
    }

    @Test
    void testReadDocumentWithSyntaxError() {
        final LoadedFile loadedFile = new StringLoadedFile("{\ninvalid syntax\n}", "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher(null, null, null);
        final InputDataException exception = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(loadedFile));
        assertThat(exception.getMessage(), equalTo("E-VSDF-1: Error in input file 'string source'."));
    }

    @ValueSource(strings = { "", " ", "   ", "\n", "\n " })
    @ParameterizedTest
    void testReadEmptyDocument(final String emptyDocumentVariant) {
        final LoadedFile loadedFile = new StringLoadedFile(emptyDocumentVariant, "string source");
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher(null, null, null);
        final InputDataException inputDataException = assertThrows(InputDataException.class,
                () -> jsonDocumentFetcher.readDocuments(loadedFile));
        assertThat(inputDataException.getMessage(), startsWith("E-VSDF-1: Error in input file 'string source'."));
    }
}