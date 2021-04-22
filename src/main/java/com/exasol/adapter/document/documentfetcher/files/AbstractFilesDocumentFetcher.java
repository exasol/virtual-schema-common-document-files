package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

import akka.NotUsed;
import akka.stream.javadsl.Source;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
@java.lang.SuppressWarnings("squid:S119") // DocumentVisitorType does not fit naming conventions.
public abstract class AbstractFilesDocumentFetcher implements DocumentFetcher {
    private static final long serialVersionUID = -7350431040429718519L;
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
    public final Source<List<FetchedDocument>, NotUsed> run(final ExaConnectionInformation connectionInformation) {
        final String prefix = connectionInformation.getAddress();
        final StringFilter filePatternWithPrefix = new PrefixPrepender().prependStaticPrefix(prefix, this.filePattern);
        final StringFilter filterWithPatternFromConnectionToPreventInjection = new StringFilterFactory()
                .and(filePatternWithPrefix, WildcardExpression.forNonWildcardPrefix(prefix));
        final Source<LoadedFile, NotUsed> fileStream = this.fileLoaderFactory
                .getLoader(filterWithPatternFromConnectionToPreventInjection, this.segmentDescription,
                        connectionInformation)
                .loadFiles();
        return fileStream.flatMapConcat(loadedFile -> readLoadedFile(loadedFile, prefix));
    }

    private Source<List<FetchedDocument>, NotUsed> readLoadedFile(final LoadedFile loadedFile, final String prefix) {
        final String relativeName = loadedFile.getResourceName().replaceFirst(Pattern.quote(prefix), "");
        return Source.fromIterator(() -> {
            final Iterator<FetchedDocument> fetchedDocuments = new TransformingIterator<>(readDocuments(loadedFile),
                    document -> new FetchedDocument(document, relativeName));
            return new ChunkBuildingIterator<>(fetchedDocuments);
        });
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
