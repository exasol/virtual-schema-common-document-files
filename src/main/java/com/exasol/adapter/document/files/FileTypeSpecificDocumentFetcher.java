package com.exasol.adapter.document.files;

import java.io.Serializable;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.iterators.CloseableIterator;

/**
 * Interface for file type specific document-fetcher implementations.
 */
public interface FileTypeSpecificDocumentFetcher extends Serializable {

    /**
     * Read the type specific document node structure from an input stream.
     * <p>
     * Close the input stream when you're done with data loading. This can not be done on a different layer, since it
     * can be delayed using streams.
     * </p>
     *
     * @param remoteFile stream of the files contents with additional description for logging
     * @return read document nodes
     */
    public CloseableIterator<DocumentNode> readDocuments(FileSegment remoteFile);

    /**
     * Get if this document fetcher can read files partially.
     * <p>
     * Supporting this feature improves performance when loading fewer files than available CPU cores in the cluster.
     * </p>
     * 
     * @return {@code true} if this document fetcher supports file splitting
     */
    public boolean supportsFileSplitting();
}
