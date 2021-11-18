package com.exasol.adapter.document.files;

import java.io.Serializable;
import java.util.Iterator;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;

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
    public Iterator<DocumentNode> readDocuments(RemoteFile remoteFile);
}
