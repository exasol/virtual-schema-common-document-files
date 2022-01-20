package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.*;

import lombok.Getter;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
public class FilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = 4639469310585326277L;
    /** @serial */
    @Getter
    private final StringFilter filePattern;
    /** @serial */
    @Getter
    private final SegmentDescription segmentDescription;
    /** @serial */
    private final FileLoaderFactory fileLoaderFactory;
    /** @serial */
    private final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher;

    /**
     * Create a new instance of {@link FilesDocumentFetcher}.
     * 
     * @param filePattern                     files to load
     * @param segmentDescription              segmentation for parallel execution
     * @param fileLoaderFactory               dependency in injection of {@link FileLoaderFactory}
     * @param fileTypeSpecificDocumentFetcher file type specific document fetcher part
     */
    public FilesDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory,
            final FileTypeSpecificDocumentFetcher fileTypeSpecificDocumentFetcher) {
        this.filePattern = filePattern;
        this.segmentDescription = segmentDescription;
        this.fileLoaderFactory = fileLoaderFactory;
        this.fileTypeSpecificDocumentFetcher = fileTypeSpecificDocumentFetcher;
    }

    @Override
    public final CloseableIterator<FetchedDocument> run(final ConnectionPropertiesReader connectionInformation) {
        final CloseableIterator<RemoteFile> files = this.fileLoaderFactory
                .getLoader(this.filePattern, connectionInformation).loadFiles();
        final SegmentMatcher segmentMatcher = SegmentMatcherFactory.buildSegmentMatcher(this.segmentDescription);
        final CloseableIterator<RemoteFile> filteredFiles = new FilteringIterator<>(files, segmentMatcher::matchesFile);
        final CloseableIterator<RemoteFile> prefetchedFiles = new RemoteFilePrefetchingIterator(filteredFiles);
        final CloseableIterator<FileSegment> segments = new FlatMapIterator<>(prefetchedFiles,
                file -> new CloseableIteratorWrapper<>(segmentMatcher.getMatchingSegmentsFor(file).iterator()));
        return new FlatMapIterator<>(segments, this::readLoadedFile);
    }

    private CloseableIterator<FetchedDocument> readLoadedFile(final FileSegment fileSegment) {
        final RemoteFile remoteFile = fileSegment.getFile();
        return new TransformingIterator<>(this.fileTypeSpecificDocumentFetcher.readDocuments(fileSegment),
                document -> new FetchedDocument(document, remoteFile.getResourceName()));
    }
}
