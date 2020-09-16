package com.exasol.adapter.document.mapping.json;

import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.mapping.*;

public class JsonPropertyToColumnValueExtractorFactory
        implements PropertyToColumnValueExtractorFactory<JsonNodeVisitor> {
    @Override
    public ColumnValueExtractor<JsonNodeVisitor> getValueExtractorForColumn(final PropertyToColumnMapping column) {
        final PropertyToColumnVisitor visitor = new PropertyToColumnVisitor();
        column.accept(visitor);
        return visitor.getColumnValueExtractor();
    }

    private static class PropertyToColumnVisitor implements PropertyToColumnMappingVisitor {
        private ColumnValueExtractor<JsonNodeVisitor> columnValueExtractor;

        @Override
        public void visit(final PropertyToVarcharColumnMapping columnDefinition) {
            this.columnValueExtractor = new JsonPropertyToVarcharColumnValueExtractor(columnDefinition);
        }

        @Override
        public void visit(final PropertyToJsonColumnMapping columnDefinition) {
            // this.columnValueExtractor = new JsonPropertyToJsonColumnValueExtractor(columnDefinition);
            throw new UnsupportedOperationException("not yet implemented");// TODO
        }

        @Override
        public void visit(final PropertyToDecimalColumnMapping columnDefinition) {
            // this.columnValueExtractor = new JsonPropertyToDecimalColumnValueExtractor(columnDefinition);
            throw new UnsupportedOperationException("not yet implemented");// TODO
        }

        public ColumnValueExtractor<JsonNodeVisitor> getColumnValueExtractor() {
            return this.columnValueExtractor;
        }
    }
}
