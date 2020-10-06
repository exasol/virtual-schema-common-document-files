package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentnode.MockValueNode;

class AbstractFilesDocumentFetcherTest {

    @Test
    void testSourceReferences() {
        final FileLoader fileLoader = mock(FileLoader.class);
        when(fileLoader.loadFiles()).thenReturn(Stream.of(mockLoadedFile("file-1"), mockLoadedFile("file-2")));
        final FileLoaderFactory loaderFactory = mock(FileLoaderFactory.class);
        when(loaderFactory.getLoader(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(fileLoader);
        final AbstractFilesDocumentFetcher<Object> documentFetcher = mock(AbstractFilesDocumentFetcher.class, Mockito
                .withSettings().useConstructor(null, null, loaderFactory).defaultAnswer(Mockito.CALLS_REAL_METHODS));
        when(documentFetcher.readDocuments(any())).thenAnswer(invocation -> Stream.of(new MockValueNode("")));
        final List<String> sourceReferences = documentFetcher.run(null).map(FetchedDocument::getSourcePath)
                .collect(Collectors.toList());
        assertThat(sourceReferences, containsInAnyOrder("file-1", "file-2"));
    }

    @NotNull
    private InputStreamWithResourceName mockLoadedFile(final String fileName) {
        return new InputStreamWithResourceName(new ByteArrayInputStream("".getBytes()), "long path", fileName);
    }
}