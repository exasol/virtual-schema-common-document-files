package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.documentfetcher.files.segmentation.ExplicitSegmentDescription;
import com.exasol.adapter.document.documentfetcher.files.segmentation.HashSegmentDescription;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;

class FilesDocumentFetcherFactoryTest {
    private static final WildcardExpression A_FILTER = WildcardExpression.fromGlob("test*");

    @Test
    void testBuildExplicitSegmentation() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(31, 3);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(3)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(0)), equalTo(11)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(1)), equalTo(10)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(2)), equalTo(10))//
        );
    }

    @Test
    void testBuildHashSegmentation() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(500, 2);
        final HashSegmentDescription segment1 = getHashSegmentDescription(documentFetchers.get(0));
        final HashSegmentDescription segment2 = getHashSegmentDescription(documentFetchers.get(1));
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(2)),
                () -> assertThat(segment1.getSegmentId(), equalTo(0)),
                () -> assertThat(segment2.getSegmentId(), equalTo(1)),
                () -> assertThat(segment1.getNumberOfSegments(), equalTo(2)),
                () -> assertThat(segment2.getNumberOfSegments(), equalTo(2))//
        );
    }

    private HashSegmentDescription getHashSegmentDescription(final DocumentFetcher documentFetcher) {
        final FilesDocumentFetcher filesDocumentFetcher = (FilesDocumentFetcher) documentFetcher;
        return (HashSegmentDescription) filesDocumentFetcher.getSegmentDescription();
    }

    private List<DocumentFetcher> runGetDocumentFetchers(final int numberOfFiles, final int maxFetcher) {
        final FileLoaderFactory fileLoaderFactory = mockFileLoaderFactory(numberOfFiles);
        final ConnectionPropertiesReader connectionInformation = mock(ConnectionPropertiesReader.class);
        final var fileTypeSpecificDocumentFetcher = mock(FileTypeSpecificDocumentFetcher.class);
        return new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(A_FILTER, maxFetcher, fileLoaderFactory,
                connectionInformation, fileTypeSpecificDocumentFetcher);
    }

    private int countFilesInExplicitSegmentDescriptions(final DocumentFetcher documentFetcher) {
        final FilesDocumentFetcher filesDocumentFetcher = (FilesDocumentFetcher) documentFetcher;
        final ExplicitSegmentDescription segmentDescription = (ExplicitSegmentDescription) filesDocumentFetcher
                .getSegmentDescription();
        return segmentDescription.getSegmentKeys().size();
    }

    private FileLoaderFactory mockFileLoaderFactory(final int numFiles) {
        final FileLoaderFactory fileLoaderFactory = mock(FileLoaderFactory.class);
        final FileLoader fileLoader = mock(FileLoader.class);
        final List<RemoteFile> remoteFiles = new ArrayList<>(numFiles);
        for (int counter = 0; counter < numFiles; counter++) {
            final RemoteFile remoteFile = mock(RemoteFile.class);
            when(remoteFile.getResourceName()).thenReturn("product-" + counter + ".json");
            remoteFiles.add(remoteFile);
        }
        when(fileLoader.loadFiles()).thenReturn(new CloseableIteratorWrapper<>(remoteFiles.iterator()));
        when(fileLoaderFactory.getLoader(any(), any())).thenReturn(fileLoader);
        return fileLoaderFactory;
    }
}