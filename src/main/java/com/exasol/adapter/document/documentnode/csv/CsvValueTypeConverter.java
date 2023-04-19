package com.exasol.adapter.document.documentnode.csv;

import java.util.List;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.mapping.ColumnMapping;

class CsvValueTypeConverter {

    private CsvValueTypeConverter() {
    }

    static CsvValueTypeConverter create(final List<ColumnMapping> csvColumns) {
        return new CsvValueTypeConverter();
    }

    DocumentNode convert(final String value, final int columnIndex) {
        return new StringHolderNode(value);
    }

    DocumentNode convert(final String value, final String columnName) {
        return new StringHolderNode(value);
    }
}
