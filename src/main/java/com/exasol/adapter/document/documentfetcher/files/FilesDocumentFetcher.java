package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentfetcher.files.segmentation.*;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.FlatMapIterator;
import com.exasol.adapter.document.iterators.TransformingIterator;

import lombok.Getter;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
public class FilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = -4885176976627358357L;
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
    public final Iterator<FetchedDocument> run(final ExaConnectionInformation connectionInformation) {
        final String prefix = connectionInformation.getAddress();
        final Iterator<RemoteFile> files = this.fileLoaderFactory.getLoader(this.filePattern, connectionInformation)
                .loadFiles();
        final SegmentMatcher segmentMatcher = SegmentMatcherFactory.buildSegmentMatcher(this.segmentDescription);
        final FlatMapIterator<FileSegment, RemoteFile> segments = new FlatMapIterator<>(files,
                file -> segmentMatcher.getMatchingSegmentsFor(file).iterator());
        return new FlatMapIterator<>(segments, segment -> readLoadedFile(segment, prefix));
    }

    private Iterator<FetchedDocument> readLoadedFile(final FileSegment fileSegment, final String prefix) {
        final RemoteFile remoteFile = fileSegment.getFile();
        final String relativeName = remoteFile.getResourceName().replaceFirst(Pattern.quote(prefix), "");
        return new TransformingIterator<>(this.fileTypeSpecificDocumentFetcher.readDocuments(fileSegment),
                document -> new FetchedDocument(document, relativeName));
    }
}
