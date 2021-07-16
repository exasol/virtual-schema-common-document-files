package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

@ExtendWith(MockitoExtension.class)
class AbstractFilesDocumentFetcherTest {
    private static final String PREFIX = "prefix/";
    @Mock
    FileLoader fileLoader;
    @Mock
    FileLoaderFactory loaderFactory;
    private ExaConnectionInformation connectionInformation;

    @BeforeEach
    void beforeEach() {
        this.connectionInformation = mockConnectionInformation();
    }

    @Test
    void testSourceReferences() throws ExecutionException, InterruptedException {
        final List<LoadedFile> loadedFiles = List.of(mockLoadedFile("file-1"), mockLoadedFile("file-2"));
        when(this.fileLoader.loadFiles()).thenReturn(loadedFiles.iterator());
        when(this.loaderFactory.getLoader(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(this.fileLoader);
        final AbstractFilesDocumentFetcher documentFetcher = mock(AbstractFilesDocumentFetcher.class,
                Mockito.withSettings()
                        .useConstructor(WildcardExpression.forNonWildcardString(""), null, this.loaderFactory)
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        when(documentFetcher.readDocuments(any()))
                .thenAnswer(invocation -> List.of(new StringHolderNode("")).iterator());
        final List<String> sourceReferences = new ArrayList<>();
        documentFetcher.run(this.connectionInformation)
                .forEachRemaining(loaded -> sourceReferences.add(loaded.getSourcePath()));
        assertThat(sourceReferences, containsInAnyOrder("file-1", "file-2"));
    }

    @Test
    void testPrefixIsAdded() {
        when(this.fileLoader.loadFiles()).thenReturn(Collections.emptyIterator());
        final ArgumentCaptor<StringFilter> argumentCaptor = ArgumentCaptor.forClass(StringFilter.class);
        when(this.loaderFactory.getLoader(argumentCaptor.capture(), Mockito.any(), Mockito.any()))
                .thenReturn(this.fileLoader);
        final AbstractFilesDocumentFetcher documentFetcher = mock(AbstractFilesDocumentFetcher.class,
                Mockito.withSettings().useConstructor(WildcardExpression.fromGlob("file-*"), null, this.loaderFactory)
                        .defaultAnswer(Mockito.CALLS_REAL_METHODS));
        documentFetcher.run(this.connectionInformation);
        assertThat(argumentCaptor.getValue().toString(), equalTo(
                "(prefix/file-<DirectoryLimitedMultiCharWildcard>) AND (prefix/<CrossDirectoryMultiCharWildcard>)"));
    }

    private ExaConnectionInformation mockConnectionInformation() {
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress()).thenReturn(PREFIX);
        return connectionInformation;
    }

    private LoadedFile mockLoadedFile(final String fileName) {
        return new StringLoadedFile("", fileName);
    }
}