package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;

/**
 * {@link FileTypeSpecificDocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = -3283964622507676609L;

    @Override
    public CloseableIterator<DocumentNode> readDocuments(final FileSegment segment) {
        if (!segment.getSegmentDescription().equals(FileSegmentDescription.ENTIRE_FILE)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-17")
                    .message("The JsonLinesDocumentFetcher does not support loading split files.").ticketMitigation()
                    .toString());
        }
        return new JsonLinesIterator(segment.getFile());
    }

    @Override
    public boolean supportsFileSplitting() {
        return false;
    }
}
