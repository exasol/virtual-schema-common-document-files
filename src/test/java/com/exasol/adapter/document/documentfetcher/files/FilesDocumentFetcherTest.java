package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.files.ConnectionInfoMockFactory.mockConnectionInfoWithAddress;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.segmentation.NoSegmentationSegmentDescription;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;

@ExtendWith(MockitoExtension.class)
class FilesDocumentFetcherTest {
    private static final String PREFIX = "prefix/";
    @Mock
    FileLoader fileLoader;
    @Mock
    FileLoaderFactory loaderFactory;
    private ExaConnectionInformation connectionInformation;

    @BeforeEach
    void beforeEach() {
        this.connectionInformation = mockConnectionInfoWithAddress(PREFIX);
    }

    @Test
    void testSourceReferences() {
        final List<RemoteFile> remoteFiles = List.of(mockLoadedFile("file-1"), mockLoadedFile("file-2"));
        when(this.fileLoader.loadFiles()).thenReturn(new CloseableIteratorWrapper<>(remoteFiles.iterator()));
        when(this.loaderFactory.getLoader(Mockito.any(), Mockito.any())).thenReturn(this.fileLoader);
        final FileTypeSpecificDocumentFetcher fileTypeSpecificFetcher = mock(FileTypeSpecificDocumentFetcher.class);
        when(fileTypeSpecificFetcher.readDocuments(any()))
                .thenAnswer(invocation -> new CloseableIteratorWrapper<>(List.of(new StringHolderNode("")).iterator()));
        final FilesDocumentFetcher documentFetcher = new FilesDocumentFetcher(
                WildcardExpression.forNonWildcardString(""), new NoSegmentationSegmentDescription(), this.loaderFactory,
                fileTypeSpecificFetcher);
        final List<String> sourceReferences = new ArrayList<>();
        documentFetcher.run(this.connectionInformation)
                .forEachRemaining(loaded -> sourceReferences.add(loaded.getSourcePath()));
        assertThat(sourceReferences, containsInAnyOrder("file-1", "file-2"));
    }

    private RemoteFile mockLoadedFile(final String fileName) {
        return new RemoteFile(fileName, 0, new StringRemoteFileContent(""));
    }
}