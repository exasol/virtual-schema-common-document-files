package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;

/**
 * {@link FileTypeSpecificDocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 1044464548669134464L;

    @Override
    public Iterator<DocumentNode> readDocuments(final RemoteFile loadedFile) {
        return new JsonLinesIterator(loadedFile);
    }
}
