package com.exasol.adapter.document.documentfetcher.files.csv;

import static com.exasol.adapter.document.documentfetcher.files.csv.CsvConfigurationHelper.getCsvConfiguration;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;

/**
 * {@link FileTypeSpecificDocumentFetcher} for CSV files.
 */
public class CsvDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 2783593249946168796L;
    /**
     * Contains additional configuration (serialised)
     */
    private String additionalConfiguration;

    /**
     * Setter for additional configuration options
     *
     * @param additionalConfiguration Setter for additional configuration
     */
    public void setAdditionalConfiguration(final String additionalConfiguration) {
        this.additionalConfiguration = additionalConfiguration;
    }

    @Override
    public CloseableIterator<DocumentNode> readDocuments(final FileSegment segment) {
        if (!segment.getSegmentDescription().equals(FileSegmentDescription.ENTIRE_FILE)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-26")
                    .message("The CsvDocumentFetcher does not support loading split files.").ticketMitigation()
                    .toString());
        }
        // documentation on readDocuments
        // https://github.com/exasol/virtual-schema-common-document-files/blob/main/doc/user_guide/document_type_plugin_development_guide.md#the-documentfetcher
        return new CsvIterator(segment.getFile(), getCsvConfiguration(this.additionalConfiguration));

    }

    @Override
    public boolean supportsFileSplitting() {
        return false;
    }

}
