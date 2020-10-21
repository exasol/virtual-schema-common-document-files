package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentnode.MockValueNode;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

class AbstractFilesDocumentFetcherTest {

    public static final String PREFIX = "prefix/";

    @Test
    void testSourceReferences() {
        final FileLoader fileLoader = mock(FileLoader.class);
        when(fileLoader.loadFiles()).thenReturn(Stream.of(mockLoadedFile("file-1"), mockLoadedFile("file-2")));
        final FileLoaderFactory loaderFactory = mock(FileLoaderFactory.class);
        when(loaderFactory.getLoader(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(fileLoader);
        final AbstractFilesDocumentFetcher<Object> documentFetcher = mock(AbstractFilesDocumentFetcher.class,
                Mockito.withSettings().useConstructor(WildcardExpression.forNonWildcardString(""), null, loaderFactory)
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        when(documentFetcher.readDocuments(any())).thenAnswer(invocation -> Stream.of(new MockValueNode("")));
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress()).thenReturn(PREFIX);
        final List<String> sourceReferences = documentFetcher.run(connectionInformation)
                .map(FetchedDocument::getSourcePath).collect(Collectors.toList());
        assertThat(sourceReferences, containsInAnyOrder("file-1", "file-2"));
    }

    @Test
    void testPrefixIsAdded() {
        final FileLoader dummyLoader = mock(FileLoader.class);
        when(dummyLoader.loadFiles()).thenReturn(Stream.of());
        final FileLoaderFactory loaderFactory = mock(FileLoaderFactory.class);
        final ArgumentCaptor<StringFilter> argumentCaptor = ArgumentCaptor.forClass(StringFilter.class);
        when(loaderFactory.getLoader(argumentCaptor.capture(), Mockito.any(), Mockito.any())).thenReturn(dummyLoader);
        final AbstractFilesDocumentFetcher<Object> documentFetcher = mock(AbstractFilesDocumentFetcher.class,
                Mockito.withSettings().useConstructor(WildcardExpression.fromGlob("file-*"), null, loaderFactory)
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress()).thenReturn(PREFIX);
        documentFetcher.run(connectionInformation);
        assertThat(argumentCaptor.getValue().toString(), equalTo(
                "(prefix/file-<DirectoryLimitedMultiCharWildcard>) AND (prefix/<CrossDirectoryMultiCharWildcard>)"));
    }

    private InputStreamWithResourceName mockLoadedFile(final String fileName) {
        return new InputStreamWithResourceName(new ByteArrayInputStream("".getBytes()), PREFIX + fileName);
    }
}