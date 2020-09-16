package com.exasol.adapter.document.json;

import com.exasol.adapter.document.AbstractDataLoaderUdf;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.mapping.PropertyToColumnValueExtractorFactory;
import com.exasol.adapter.document.mapping.json.JsonPropertyToColumnValueExtractorFactory;

/**
 * This class is the UDF entry point for the data loading from JSON files.
 */
public class JsonDataLoaderUdf extends AbstractDataLoaderUdf<JsonNodeVisitor> {
    @Override
    protected PropertyToColumnValueExtractorFactory<JsonNodeVisitor> getValueExtractorFactory() {
        return new JsonPropertyToColumnValueExtractorFactory();
    }
}
