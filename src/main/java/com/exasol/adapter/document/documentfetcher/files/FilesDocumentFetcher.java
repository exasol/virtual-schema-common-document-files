package com.exasol.adapter.document.documentfetcher.files;

import java.util.logging.Level;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentfetcher.files.csv.CsvDocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.*;
import com.exasol.logging.RemoteLogManager;

/**
 * This is a basis for {@link DocumentFetcher}s that fetches data/documents from files.
 */
public class FilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = 3556762980241219699L;
    /** @serial */
    private final StringFilter filePattern;
    /** @serial */
    private final SegmentDescription segmentDescription;
    /** @serial */
    private final FileFinderFactory fileFinderFactory;
    /** @serial */
    private final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher;
    /** @serial */
    private final String additionalConfiguration;

    /**
     * Create a new instance of {@link FilesDocumentFetcher}.
     *
     * @param filePattern                     files to load
     * @param segmentDescription              segmentation for parallel execution
     * @param fileFinderFactory               dependency in injection of {@link FileFinderFactory}
     * @param fileTypeSpecificDocumentFetcher file type specific document fetcher part
     * @param additionalConfiguration         additional configuration
     */
    public FilesDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileFinderFactory fileFinderFactory,
            final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher,
            final String additionalConfiguration) {
        this.filePattern = filePattern;
        this.segmentDescription = segmentDescription;
        this.fileFinderFactory = fileFinderFactory;
        this.fileTypeSpecificDocumentFetcher = fileTypeSpecificDocumentFetcher;
        this.additionalConfiguration = additionalConfiguration;
    }

    /**
     * Get the file pattern.
     *
     * @return file pattern
     */
    public StringFilter getFilePattern() {
        return this.filePattern;
    }

    /**
     * Get the segment description.
     *
     * @return segment description
     */
    public SegmentDescription getSegmentDescription() {
        return this.segmentDescription;
    }

    @Override
    public final CloseableIterator<FetchedDocument> run(final ConnectionPropertiesReader connectionInformation) {
        new RemoteLogManager().setupConsoleLogger(Level.ALL);
        final RemoteFileFinder remoteFileFinder = this.fileFinderFactory.getFinder(this.filePattern,
                connectionInformation);
        final CloseableIterator<RemoteFile> files = remoteFileFinder.loadFiles();
        final SegmentMatcher segmentMatcher = SegmentMatcherFactory.buildSegmentMatcher(this.segmentDescription);
        final CloseableIterator<RemoteFile> filteredFiles = new FilteringIterator<>(files, segmentMatcher::matchesFile);
        final CloseableIterator<RemoteFile> prefetchedFiles = new RemoteFilePrefetchingIterator(filteredFiles);
        final CloseableIterator<FileSegment> segments = new FlatMapIterator<>(prefetchedFiles,
                file -> new CloseableIteratorWrapper<>(segmentMatcher.getMatchingSegmentsFor(file).iterator()));
        return new FlatMapIterator<>(segments, this::readLoadedFile);
    }

    private CloseableIterator<FetchedDocument> readLoadedFile(final FileSegment fileSegment) {
        final RemoteFile remoteFile = fileSegment.getFile();
        // add the property if csv
        if (this.fileTypeSpecificDocumentFetcher instanceof CsvDocumentFetcher) {
            final CsvDocumentFetcher csvDocumentFetcher = (CsvDocumentFetcher) this.fileTypeSpecificDocumentFetcher;
            csvDocumentFetcher.setAdditionalConfiguration(this.additionalConfiguration);
        }
        return new TransformingIterator<>(this.fileTypeSpecificDocumentFetcher.readDocuments(fileSegment),
                document -> new FetchedDocument(document, remoteFile.getResourceName()));
    }
}
