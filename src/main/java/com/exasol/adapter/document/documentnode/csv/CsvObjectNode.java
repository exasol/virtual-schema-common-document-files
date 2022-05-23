package com.exasol.adapter.document.documentnode.csv;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.holder.StringHolderNode;
import com.exasol.adapter.document.documentnode.json.JsonNodeFactory;
import de.siegmar.fastcsv.reader.CsvRow;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents a single CSV Row.
 */
public class CsvObjectNode implements DocumentObject {
    //todo: in my case this would probably be a CSV row
    //private final CsvObject csvObject;
    private final CsvRow csvRow;
    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param csvRow CSV row to wrap
     */
    public CsvObjectNode(final CsvRow csvRow) {
        this.csvRow = csvRow;
    }
    //simplest approach, use the index for now
    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        Map<String, DocumentNode> map = new HashMap();
        int listIndex = 0;
        for (String value :this.csvRow.getFields()){
            map.put(String.valueOf(listIndex), new StringHolderNode(value));
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        //if it's not using headers you can use indices since the internals are a list<String> to store the fields, otherwise there's a NamedCsvReader getField
        int index = Integer.parseInt(key);
        return new StringHolderNode(csvRow.getFields().get(index));
        //return JsonNodeFactory.getInstance().getCsvNode(this.csvRow.get(key));

    }

    @Override
    public boolean hasKey(final String key) {
        return Integer. parseInt(key) < (csvRow.getFieldCount()-1);
        //return this.csvRow.containsKey(key);
    }
}
