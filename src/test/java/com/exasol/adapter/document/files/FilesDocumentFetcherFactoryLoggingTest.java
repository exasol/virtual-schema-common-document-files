package com.exasol.adapter.document.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.segmentation.HashSegmentDescription;
import com.exasol.adapter.document.documentfetcher.files.segmentation.SegmentDescription;

class FilesDocumentFetcherFactoryLoggingTest {

    private Logger mockLogger;
    private FilesDocumentFetcherFactory factory;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class);
        when(mockLogger.isLoggable(Level.FINE)).thenReturn(true);
        factory = new FilesDocumentFetcherFactory(mockLogger);
    }

    @Test
    void testBuildExplicitSegmentation_logsExpectedMessages() {
        List<RemoteFile> files = List.of(
                new RemoteFile("file1", 10_000_000, null),
                new RemoteFile("file2", 5_000_000, null)
        );

        factory.buildExplicitSegmentation(4, files, true);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Supplier<String>> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(mockLogger, atLeast(1)).fine(captor.capture());

        String joinedLogs = captor.getAllValues().stream()
                .map(Supplier::get)
                .reduce("", (a, b) -> a + "\n" + b);

        assertTrue(joinedLogs.contains("Starting explicit segmentation"));
        assertTrue(joinedLogs.contains("Calculated number of workers"));
        assertTrue(joinedLogs.contains("Number of file segments after splitting"));
        assertTrue(joinedLogs.contains("Distributed file segments into"));
        assertTrue(joinedLogs.contains("Created segment for bin"));
        assertTrue(joinedLogs.contains("Completed building"));
    }

    @Test
    void testBuildHashSegmentation_logsExpectedMessages() {
        List<SegmentDescription> result = factory.buildHashSegmentation(3);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Supplier<String>> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(mockLogger, atLeast(1)).fine(captor.capture());

        String joinedLogs = captor.getAllValues().stream()
                .map(Supplier::get)
                .reduce("", (a, b) -> a + "\n" + b);

        assertTrue(joinedLogs.contains("Starting to build hash segmentation"));
        assertTrue(joinedLogs.contains("Created hash segment description with counter"));
        assertTrue(joinedLogs.contains("Completed building hash segmentation"));

        assertEquals(3, result.size());
        assertTrue(result.get(0) instanceof HashSegmentDescription);
    }
}