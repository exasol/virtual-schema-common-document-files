package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.errorreporting.ExaError;

/**
 * {@link FileTypeSpecificDocumentFetcher} for the JSON lines file format.
 */
public class JsonLinesDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = -4124591355499525541L;

    @Override
    public Iterator<DocumentNode> readDocuments(final FileSegment segment) {
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
