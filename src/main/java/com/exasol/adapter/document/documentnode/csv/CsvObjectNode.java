package com.exasol.adapter.document.documentnode.csv;

import java.util.*;

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
            map.put(String.valueOf(listIndex), this.typeConverter.convert(value, listIndex));
            listIndex++;
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        final int index = convertIndex(key);
        if (index >= this.row.getFieldCount()) {
            throw new NoSuchElementException("No element with name '" + index + "' found.");
        }
        final String value = this.row.getField(index);
        return this.typeConverter.convert(value, index);
    }

    private int convertIndex(final String key) {
        try {
            return Integer.parseInt(key);
        } catch (final NumberFormatException exception) {
            throw new IllegalArgumentException("No element with name '" + key + "' found.", exception);
        }
    }

    @Override
    public boolean hasKey(final String key) {
        try {
            return Integer.parseInt(key) < this.row.getFieldCount();
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
