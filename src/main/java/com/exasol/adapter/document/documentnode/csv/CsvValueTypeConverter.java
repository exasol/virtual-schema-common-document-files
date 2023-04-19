package com.exasol.adapter.document.documentnode.csv;

import java.util.List;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.mapping.ColumnMapping;

class CsvValueTypeConverter {

    CsvValueTypeConverter() {
    }

    CsvValueTypeConverter(final List<ColumnMapping> csvColumns) {
    }

    public DocumentNode convert(final String value, final int columnIndex) {
        return new StringHolderNode(value);
    }

    public DocumentNode convert(final String value, final String columnName) {
        return new StringHolderNode(value);
    }
}
