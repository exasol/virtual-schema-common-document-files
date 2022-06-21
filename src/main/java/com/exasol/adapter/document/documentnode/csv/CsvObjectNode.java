package com.exasol.adapter.document.documentnode.csv;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.reader.NamedCsvRow;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a single CSV Row.
 */
public class CsvObjectNode implements DocumentObject {
    //This represents a Csv 'Row', 1 line of separated csv values
    private CsvRow csvRow;
    private NamedCsvRow namedCsvRow;
    private boolean hasHeaders;
    private boolean useHeadersForMapping;
    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param csvRow CSV row to wrap
     */
    public CsvObjectNode(final CsvRow csvRow) {
        this.csvRow = csvRow;
        this.hasHeaders=false;
    }
    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param csvRow Named CSV row to wrap
     */
    public CsvObjectNode(final NamedCsvRow csvRow) {
        this.namedCsvRow = csvRow;
        this.hasHeaders=true;
    }
    //simplest approach, use the index for now
    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        if (!hasHeaders) {
            Map<String, DocumentNode> map = new HashMap();
            int listIndex = 0;
            for (String value : this.csvRow.getFields()) {
                map.put(String.valueOf(listIndex), new StringHolderNode(value));
                listIndex++;
            }
            return map;
        } else {
                Map<String, DocumentNode> map = new HashMap();
                //int listIndex = 0;
                for (Map.Entry<String, String> entryset : this.namedCsvRow.getFields().entrySet()) {
                    map.put(String.valueOf(entryset.getKey()), new StringHolderNode(entryset.getValue()));
                    //listIndex++;
                }
                return map;
            }
        }

    @Override
    public DocumentNode get(final String key) {
        if (!hasHeaders) {
            //if it's not using headers you can use indices since the internals are a list<String> to store the fields, otherwise there's a NamedCsvReader getField
            int index = Integer.parseInt(key);
            return new StringHolderNode(csvRow.getFields().get(index));
            //return new StringHolderNode(csvRow.getField(index));
        } else {
                return new StringHolderNode(namedCsvRow.getField(key));
                }
        }

    @Override
    public boolean hasKey(final String key) {
            if (!hasHeaders) {
                try {
                    return Integer.parseInt(key) < (csvRow.getFieldCount());
                } catch (NumberFormatException e) {
                    return false;
                }
            } else {
                        return namedCsvRow.getFields().containsKey(key);
            }
    }
}
