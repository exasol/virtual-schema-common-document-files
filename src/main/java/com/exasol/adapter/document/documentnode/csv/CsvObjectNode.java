package com.exasol.adapter.document.documentnode.csv;

import java.util.*;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueTypeConverterRegistry;
import com.exasol.adapter.document.documentnode.csv.converter.ValueConverter;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.CsvRow;

/**
 * This class represents a single CSV Row.
 */
class CsvObjectNode implements DocumentObject {

    private final CsvRow row;
    private final CsvValueTypeConverterRegistry typeConverter;
    private final String resourceName;

    /**
     * Create a new instance of {@link CsvObjectNode}.
     *
     * @param resourceName  the resource name or file path of the CSV file
     * @param typeConverter the converter for converting CSV values to {@link DocumentNode}
     * @param row           CSV row to wrap
     */
    CsvObjectNode(final String resourceName, final CsvValueTypeConverterRegistry typeConverter, final CsvRow row) {
        this.resourceName = resourceName;
        this.typeConverter = typeConverter;
        this.row = row;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        int listIndex = 0;
        for (final String value : this.row.getFields()) {
            map.put(String.valueOf(listIndex), convert(listIndex, value));
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
        return convert(index, value);
    }

    private DocumentNode convert(final int index, final String value) {
        final ValueConverter converter = this.typeConverter.findConverter(index);
        try {
            return converter.convert(value);
        } catch (final RuntimeException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-66").message(
                    "Error converting value {{value}} using converter {{converter|u}} (file {{resource name}}, row {{line number}}, column {{column index}}).")
                    .parameter("value", value, "value to convert") //
                    .parameter("converter", converter, "converter that was used for converting the value")
                    .parameter("resource name", this.resourceName, "resource name or path to the CSV file")
                    .parameter("line number", this.row.getOriginalLineNumber(),
                            "line number of the value (starting with 1)")
                    .parameter("column index", index, "column index of the value (starting with 0)") //
                    .mitigation(
                            "Please fix the value in the CSV file or choose a different mapping for converting the value.")
                    .toString(), exception);
        }
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
