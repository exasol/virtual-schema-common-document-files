package com.exasol.adapter.document.files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.segmentation.HashSegmentDescription;
import com.exasol.adapter.document.documentfetcher.files.segmentation.SegmentDescription;

@ExtendWith(MockitoExtension.class)
class FilesDocumentFetcherFactoryLoggingTest {

    @Mock
    Logger mockLogger;

    private FilesDocumentFetcherFactory factory;

    @BeforeEach
    void setUp() {
        // The class under test now takes a logger directly — good!
        when(mockLogger.isLoggable(Level.FINE)).thenReturn(true);
        factory = new FilesDocumentFetcherFactory(mockLogger);
    }

    @Test
    void testBuildExplicitSegmentation_logsExpectedMessages() {
        List<RemoteFile> files = List.of(
                new RemoteFile("file1", 10_000_000, null),
                new RemoteFile("file2", 5_000_000, null)
        );

        List<SegmentDescription> result = factory.buildExplicitSegmentation(4, files, true);

        verify(mockLogger, atLeastOnce()).fine(contains("Starting explicit segmentation"));
        verify(mockLogger, atLeastOnce()).fine(contains("Calculated number of workers"));
        verify(mockLogger, atLeastOnce()).fine(contains("Number of file segments after splitting"));
        verify(mockLogger, atLeastOnce()).fine(contains("Distributed file segments into"));
        verify(mockLogger, atLeastOnce()).fine(contains("Created segment for bin"));
        verify(mockLogger, atLeastOnce()).fine(contains("Completed building"));

        assertFalse(result.isEmpty());
    }

    @Test
    void testBuildHashSegmentation_logsExpectedMessages() {
        List<SegmentDescription> result = factory.buildHashSegmentation(3);

        verify(mockLogger, atLeastOnce()).fine(contains("Starting to build hash segmentation"));
        verify(mockLogger, atLeast(3)).fine(contains("Created hash segment description with counter"));
        verify(mockLogger, atLeastOnce()).fine(contains("Completed building hash segmentation"));

        assertEquals(3, result.size());
        assertTrue(result.get(0) instanceof HashSegmentDescription);
    }
}