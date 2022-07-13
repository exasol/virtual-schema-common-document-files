package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.StringReader;

import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.errorreporting.ExaError;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * {@link FileTypeSpecificDocumentFetcher} for CSV files.
 */
public class CsvDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 2783593249946168796L;
    private String additionalConfiguration;

    /**
     * @param additionalConfiguration Setter for additional configuration
     */
    public void setAdditionalConfiguration(String additionalConfiguration) {
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
        return new CsvIterator(segment.getFile(), getCsvConfiguration());

    }

    private CsvConfiguration getCsvConfiguration() {
        if (additionalConfiguration != null && !additionalConfiguration.isEmpty()) {
            try (JsonReader jsonReader = Json.createReader(new StringReader(additionalConfiguration))) {
                JsonObject additionalConfigurationJson = jsonReader.readObject();
                boolean hasHeaders = additionalConfigurationJson.getBoolean("csv-headers", false);
                return new CsvConfiguration(hasHeaders);
            }
        }
        return null;
    }

    @Override
    public boolean supportsFileSplitting() {
        return false;
    }

}
