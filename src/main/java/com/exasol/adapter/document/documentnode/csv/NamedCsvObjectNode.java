package com.exasol.adapter.document.documentnode.csv;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;

import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This class represents a single CSV Row with named columns.
 */
public class NamedCsvObjectNode implements DocumentObject {
    private final NamedCsvRow namedCsvRow;

    /**
     * Create a new instance of {@link NamedCsvObjectNode}.
     *
     * @param csvRow Named CSV row to wrap
     */
    public NamedCsvObjectNode(final NamedCsvRow csvRow) {
        this.namedCsvRow = csvRow;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        for (final Map.Entry<String, String> entrySet : this.namedCsvRow.getFields().entrySet()) {
            map.put(String.valueOf(entrySet.getKey()), new StringHolderNode(entrySet.getValue()));
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        return new StringHolderNode(namedCsvRow.getField(key));
    }

    @Override
    public boolean hasKey(final String key) {
        return namedCsvRow.getFields().containsKey(key);
    }
}
