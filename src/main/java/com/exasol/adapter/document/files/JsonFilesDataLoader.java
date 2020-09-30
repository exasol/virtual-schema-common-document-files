package com.exasol.adapter.document.files;

import com.exasol.adapter.document.AbstractDataLoader;
import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.mapping.PropertyToColumnValueExtractorFactory;
import com.exasol.adapter.document.mapping.json.JsonPropertyToColumnValueExtractorFactory;

/**
 * {@link DataLoader} for JSON documents.
 */
public class JsonFilesDataLoader extends AbstractDataLoader<JsonNodeVisitor> {
    private static final long serialVersionUID = 4891180789265505288L;

    /**
     * Create a new instance of {@link JsonFilesDataLoader}.
     *
     * @param documentFetcher document fetcher that provides the document data.
     */
    public JsonFilesDataLoader(final DocumentFetcher<JsonNodeVisitor> documentFetcher) {
        super(documentFetcher);
    }

    @Override
    protected PropertyToColumnValueExtractorFactory<JsonNodeVisitor> getValueExtractorFactory() {
        return new JsonPropertyToColumnValueExtractorFactory();
    }
}
