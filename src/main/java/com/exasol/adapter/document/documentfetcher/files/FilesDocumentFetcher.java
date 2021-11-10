package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentfetcher.files.segmentation.SegmentDescription;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.FlatMapIterator;
import com.exasol.adapter.document.iterators.TransformingIterator;

import lombok.Getter;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
public class FilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = -4438202224871103848L;
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
        final Iterator<RemoteFile> files = this.fileLoaderFactory
                .getLoader(this.filePattern, this.segmentDescription, connectionInformation).loadFiles();
        return new FlatMapIterator<>(files, remoteFile -> readLoadedFile(remoteFile, prefix));
    }

    private Iterator<FetchedDocument> readLoadedFile(final RemoteFile remoteFile, final String prefix) {
        final String relativeName = remoteFile.getResourceName().replaceFirst(Pattern.quote(prefix), "");
        return new TransformingIterator<>(this.fileTypeSpecificDocumentFetcher.readDocuments(remoteFile),
                document -> new FetchedDocument(document, relativeName));
    }
}
