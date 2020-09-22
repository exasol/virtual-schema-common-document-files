package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.exasol.ExaConnectionInformation;

class JsonDocumentFetcherTest {

    @Test
    void testClosed() {
        final CloseCheckStream closeCheckStream = new CloseCheckStream("{}");
        final FileLoader fileLoader = mock(FileLoader.class);
        when(fileLoader.loadFiles()).thenReturn(Stream.of(closeCheckStream));
        final FileLoaderFactory loaderFactory = mock(FileLoaderFactory.class);
        when(loaderFactory.getLoader(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(fileLoader);
        final JsonDocumentFetcher jsonDocumentFetcher = new JsonDocumentFetcher("", null, loaderFactory);
        jsonDocumentFetcher.run(mock(ExaConnectionInformation.class)).forEach(x -> {
        });
        assertThat(closeCheckStream.wasClosed(), equalTo(true));
    }
}