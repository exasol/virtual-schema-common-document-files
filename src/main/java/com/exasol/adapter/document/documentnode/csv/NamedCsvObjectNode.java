package com.exasol.adapter.document.documentnode.csv;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This class represents a single CSV Row with named columns.
 */
class NamedCsvObjectNode implements DocumentObject {
    private final NamedCsvRow row;
    private final CsvValueTypeConverter typeConverter;

    /**
     * Create a new instance of {@link NamedCsvObjectNode}.
     * 
     * @param typeConverter
     * @param row           the named CSV row to wrap
     */
    NamedCsvObjectNode(final CsvValueTypeConverter typeConverter, final NamedCsvRow row) {
        this.typeConverter = typeConverter;
        this.row = row;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        for (final Map.Entry<String, String> entrySet : this.row.getFields().entrySet()) {
            map.put(String.valueOf(entrySet.getKey()), typeConverter.convert(entrySet.getValue(), entrySet.getKey()));
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        return typeConverter.convert(row.getField(key), key);
    }

    @Override
    public boolean hasKey(final String key) {
        return row.getFields().containsKey(key);
    }
}
