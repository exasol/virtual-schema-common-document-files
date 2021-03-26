package com.exasol.adapter.document.documentfetcher.files;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.FetchedDocument;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
@java.lang.SuppressWarnings("squid:S119") // DocumentVisitorType does not fit naming conventions.
public abstract class AbstractFilesDocumentFetcher<DocumentVisitorType>
        implements DocumentFetcher<DocumentVisitorType> {
    private static final long serialVersionUID = -4554289393581677748L;
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
    public final Stream<FetchedDocument<DocumentVisitorType>> run(
            final ExaConnectionInformation connectionInformation) {
        final String prefix = connectionInformation.getAddress();
        final StringFilter filePatternWithPrefix = new PrefixPrepender().prependStaticPrefix(prefix, this.filePattern);
        final StringFilter filterWithPatternFromConnectionToPreventInjection = new StringFilterFactory()
                .and(filePatternWithPrefix, WildcardExpression.forNonWildcardPrefix(prefix));
        final Stream<LoadedFile> fileStream = this.fileLoaderFactory
                .getLoader(filterWithPatternFromConnectionToPreventInjection, this.segmentDescription,
                        connectionInformation)
                .loadFiles();
        return fileStream.flatMap(loadedFile -> readLoadedFile(loadedFile, prefix));
    }

    private Stream<FetchedDocument<DocumentVisitorType>> readLoadedFile(final LoadedFile loadedFile,
            final String prefix) {
        final String relativeName = loadedFile.getResourceName().replaceFirst(Pattern.quote(prefix), "");
        return readDocuments(loadedFile).map(document -> new FetchedDocument<>(document, relativeName));
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
    protected abstract Stream<DocumentNode<DocumentVisitorType>> readDocuments(LoadedFile loadedFile);
}
