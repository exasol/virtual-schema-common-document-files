package com.exasol.adapter.document.documentnode.csv;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.CsvRow;

/**
 * This class represents a single CSV Row.
 */
public class CsvObjectNode implements DocumentObject {

    private final CsvRow csvRow;

    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param csvRow CSV row to wrap
     */
    public CsvObjectNode(final CsvRow csvRow) {
        this.csvRow = csvRow;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        int listIndex = 0;
        for (final String value : this.csvRow.getFields()) {
            map.put(String.valueOf(listIndex), new StringHolderNode(value));
            listIndex++;
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        // if it's not using headers you can use indices since the internals are a list<String> to store the fields,
        final int index = Integer.parseInt(key);
        return new StringHolderNode(csvRow.getFields().get(index));
    }

    @Override
    public boolean hasKey(final String key) {
        try {
            return Integer.parseInt(key) < (csvRow.getFieldCount());
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
