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
    private static final int LARGE_FILE = 1_000_000_000;

    @Test
    void testBuildExplicitSegmentation() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(31, 3, false, LARGE_FILE);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(3)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(0)), equalTo(11)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(1)), equalTo(10)),
                () -> assertThat(countFilesInExplicitSegmentDescriptions(documentFetchers.get(2)), equalTo(10))//
        );
    }

    @Test
    void testBuildExplicitSegmentationWithLessFilesThanBins() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(3, 30, false, LARGE_FILE);
        assertThat(documentFetchers.size(), equalTo(3));
    }

    @Test
    void testSegmentation() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(3, 30, true, LARGE_FILE);
        assertThat(documentFetchers.size(), equalTo(30));
    }

    @Test
    void testSmallFiles() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(3, 30, true, 1);
        assertThat(documentFetchers.size(), equalTo(1));
    }

    @Test
    void testBuildHashSegmentation() {
        final List<DocumentFetcher> documentFetchers = runGetDocumentFetchers(500, 2, false, LARGE_FILE);
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

    private List<DocumentFetcher> runGetDocumentFetchers(final int numberOfFiles, final int maxFetcher,
            final boolean supportsFileSplitting, final long fileSize) {
        final FileFinderFactory fileFinderFactory = mockFileLoaderFactory(numberOfFiles, fileSize);
        final ConnectionPropertiesReader connectionInformation = mock(ConnectionPropertiesReader.class);
        final var fileTypeSpecificDocumentFetcher = mock(FileTypeSpecificDocumentFetcher.class);
        when(fileTypeSpecificDocumentFetcher.supportsFileSplitting()).thenReturn(supportsFileSplitting);
        return new FilesDocumentFetcherFactory().buildDocumentFetcherForQuery(A_FILTER, maxFetcher, fileFinderFactory,
                connectionInformation, fileTypeSpecificDocumentFetcher);
    }

    private int countFilesInExplicitSegmentDescriptions(final DocumentFetcher documentFetcher) {
        final FilesDocumentFetcher filesDocumentFetcher = (FilesDocumentFetcher) documentFetcher;
        final ExplicitSegmentDescription segmentDescription = (ExplicitSegmentDescription) filesDocumentFetcher
                .getSegmentDescription();
        return segmentDescription.getSegmentKeys().size();
    }

    private FileFinderFactory mockFileLoaderFactory(final int numFiles, final long fileSize) {
        final FileFinderFactory fileFinderFactory = mock(FileFinderFactory.class);
        final RemoteFileFinder remoteFileFinder = mock(RemoteFileFinder.class);
        final List<RemoteFile> remoteFiles = new ArrayList<>(numFiles);
        for (int counter = 0; counter < numFiles; counter++) {
            final RemoteFile remoteFile = mock(RemoteFile.class);
            when(remoteFile.getResourceName()).thenReturn("product-" + counter + ".json");
            when(remoteFile.getSize()).thenReturn(fileSize);
            remoteFiles.add(remoteFile);
        }
        when(remoteFileFinder.loadFiles()).thenReturn(new CloseableIteratorWrapper<>(remoteFiles.iterator()));
        when(fileFinderFactory.getFinder(any(), any())).thenReturn(remoteFileFinder);
        return fileFinderFactory;
    }
}