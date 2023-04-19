package com.exasol.adapter.document.documentnode.csv;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;

import de.siegmar.fastcsv.reader.CsvRow;

/**
 * This class represents a single CSV Row.
 */
class CsvObjectNode implements DocumentObject {

    private final CsvRow row;
    private final CsvValueTypeConverter typeConverter;

    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param row CSV row to wrap
     */
    CsvObjectNode(final CsvValueTypeConverter typeConverter, final CsvRow row) {
        this.typeConverter = typeConverter;
        this.row = row;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        int listIndex = 0;
        for (final String value : this.row.getFields()) {
            map.put(String.valueOf(listIndex), typeConverter.convert(value, listIndex));
            listIndex++;
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        // if it's not using headers you can use indices since the internals are a list<String> to store the fields,
        final int index = Integer.parseInt(key);
        return typeConverter.convert(row.getFields().get(index), index);
    }

    @Override
    public boolean hasKey(final String key) {
        try {
            return Integer.parseInt(key) < (row.getFieldCount());
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
