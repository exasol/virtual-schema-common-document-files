package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import com.exasol.adapter.document.iterators.FlatMapIterator;
import com.exasol.adapter.document.iterators.TransformingIterator;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
public abstract class AbstractFilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = -6407330666075977620L;
    /** @serial */
    private final StringFilter filePattern;
    /** @serial */
    private final SegmentDescription segmentDescription;
    /** @serial */
    private final FileLoaderFactory fileLoaderFactory;

    /**
     * Create a new instance of {@link AbstractFilesDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     */
    protected AbstractFilesDocumentFetcher(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        this.filePattern = filePattern;
        this.segmentDescription = segmentDescription;
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public final Iterator<FetchedDocument> run(final ExaConnectionInformation connectionInformation) {
        final String prefix = connectionInformation.getAddress();
        final StringFilter filePatternWithPrefix = new PrefixPrepender().prependStaticPrefix(prefix, this.filePattern);
        final StringFilter filterWithPatternFromConnectionToPreventInjection = new StringFilterFactory()
                .and(filePatternWithPrefix, WildcardExpression.forNonWildcardPrefix(prefix));
        final Iterator<LoadedFile> files = this.fileLoaderFactory
                .getLoader(filterWithPatternFromConnectionToPreventInjection, this.segmentDescription,
                        connectionInformation)
                .loadFiles();
        return new FlatMapIterator<>(files, loadedFile -> readLoadedFile(loadedFile, prefix));
    }

    private Iterator<FetchedDocument> readLoadedFile(final LoadedFile loadedFile, final String prefix) {
        final String relativeName = loadedFile.getResourceName().replaceFirst(Pattern.quote(prefix), "");
        return new TransformingIterator<>(readDocuments(loadedFile),
                document -> new FetchedDocument(document, relativeName));
    }

    /**
     * Read the type specific document node structure from an input stream.
     * <p>
     * Close the input stream when you're done with data loading. This can not be done on a different layer, since it
     * can be delayed using streams.
     * </p>
     * 
     * @param loadedFile stream of the files contents with additional description for logging
     * @return read document nodes
     */
    protected abstract Iterator<DocumentNode> readDocuments(LoadedFile loadedFile);
}
