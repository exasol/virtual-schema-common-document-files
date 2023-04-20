package com.exasol.adapter.document.documentnode.csv;

import java.util.HashMap;
import java.util.Map;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.CsvValueTypeConverter.ValueConverter;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This class represents a single CSV Row with named columns.
 */
class NamedCsvObjectNode implements DocumentObject {
    private final NamedCsvRow row;
    private final CsvValueTypeConverter typeConverter;
    private final String resourceName;

    /**
     * Create a new instance of {@link NamedCsvObjectNode}.
     *
     * @param resourceName  the resource name or file path of the CSV file
     * @param typeConverter the converter for converting CSV values to {@link DocumentNode}
     * @param row           the named CSV row to wrap
     */
    NamedCsvObjectNode(final String resourceName, final CsvValueTypeConverter typeConverter, final NamedCsvRow row) {
        this.resourceName = resourceName;
        this.typeConverter = typeConverter;
        this.row = row;
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        for (final Map.Entry<String, String> entrySet : this.row.getFields().entrySet()) {
            map.put(String.valueOf(entrySet.getKey()), convert(entrySet.getKey(), entrySet.getValue()));
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        return convert(key, this.row.getField(key));
    }

    private DocumentNode convert(final String key, final String value) {
        final ValueConverter converter = this.typeConverter.findConverter(key);
        try {
            return converter.convert(value);
        } catch (final RuntimeException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-67").message(
                    "Error converting value {{value}} using converter {{converter|u}} (file {{resource name}}, row {{line number}}, column {{column name}}).")
                    .parameter("value", value, "value to convert") //
                    .parameter("converter", converter, "converter that was used for converting the value")
                    .parameter("resource name", this.resourceName, "resource name or path to the CSV file")
                    .parameter("line number", this.row.getOriginalLineNumber(),
                            "line number of the value (starting with 1)")
                    .parameter("column name", key, "column name of the value") //
                    .mitigation(
                            "Please fix the value in the CSV file or chose a different mapping for converting the value.")
                    .toString(), exception);
        }
    }

    @Override
    public boolean hasKey(final String key) {
        return this.row.getFields().containsKey(key);
    }
}
