package com.exasol.adapter.document.documentfetcher.files;

import java.util.stream.Stream;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.DocumentNode;

/**
 * This is an abstract basis for {@link DocumentFetcher}s that fetch data from files.
 */
@java.lang.SuppressWarnings("squid:S119") // DocumentVisitorType does not fit naming conventions.
public abstract class AbstractFilesDocumentFetcher<DocumentVisitorType>
        implements DocumentFetcher<DocumentVisitorType> {
    private static final long serialVersionUID = -2604354872947669906L;
    private final String filePattern;
    private final SegmentDescription segmentDescription;
    private final FileLoaderFactory fileLoaderFactory;

    /**
     * Create a new instance of {@link AbstractFilesDocumentFetcher}.
     * 
     * @param filePattern        files to load
     * @param fileLoaderFactory  dependency in injection of {@link FileLoaderFactory}.
     * @param segmentDescription segmentation for parallel execution
     */
    protected AbstractFilesDocumentFetcher(final String filePattern, final SegmentDescription segmentDescription,
            final FileLoaderFactory fileLoaderFactory) {
        this.filePattern = filePattern;
        this.segmentDescription = segmentDescription;
        this.fileLoaderFactory = fileLoaderFactory;
    }

    @Override
    public final Stream<DocumentNode<DocumentVisitorType>> run(final ExaConnectionInformation connectionInformation) {
        final Stream<InputStreamWithResourceName> jsonStream = this.fileLoaderFactory
                .getLoader(this.filePattern, this.segmentDescription, connectionInformation).loadFiles();
        return jsonStream.flatMap(this::readDocuments);
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
    protected abstract Stream<DocumentNode<DocumentVisitorType>> readDocuments(InputStreamWithResourceName loadedFile);
}
