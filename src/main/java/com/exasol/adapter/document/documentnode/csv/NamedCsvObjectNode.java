package com.exasol.adapter.document.documentnode.csv;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentObject;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueConverter;
import com.exasol.adapter.document.documentnode.csv.converter.CsvValueConverters;
import com.exasol.errorreporting.ExaError;

import de.siegmar.fastcsv.reader.NamedCsvRow;

/**
 * This class represents a single CSV Row with named columns.
 */
class NamedCsvObjectNode implements DocumentObject {
    private static final Logger LOG = Logger.getLogger(NamedCsvObjectNode.class.getName());
    private final CsvValueConverters converters;
    private final String resourceName;
    private final Map<String, String> fields;
    private final long originalLineNumber;

    /**
     * Create a new instance of {@link NamedCsvObjectNode}.
     *
     * @param resourceName the resource name or file path of the CSV file
     * @param converters   the converters for converting CSV values to {@link DocumentNode}
     * @param row          the named CSV row to wrap
     */
    NamedCsvObjectNode(final String resourceName, final CsvValueConverters converters, final NamedCsvRow row) {
        this.resourceName = resourceName;
        this.converters = converters;
        this.fields = trimmedFieldNames(row);
        this.originalLineNumber = row.getOriginalLineNumber();
    }

    private Map<String, String> trimmedFieldNames(final NamedCsvRow row) {
        ensureUniqueFieldNames(row.getFields().keySet());
        return row.getFields().entrySet().stream() //
                .collect(toMap(entry -> entry.getKey().trim(), Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }

    private void ensureUniqueFieldNames(final Set<String> keys) {
        final Set<String> unifiedNames = keys.stream().map(NamedCsvObjectNode::unifyFieldName).collect(toSet());
        if (unifiedNames.size() != keys.size()) {
            final String quotedKeys = keys.stream().map(key -> "'" + key + "'").collect(joining(", "));
            throw new IllegalStateException(ExaError.messageBuilder("E-VSDF-69")
                    .message("CSV file {{file path}} contains headers with duplicate names: [{{header names|u}}].",
                            resourceName, quotedKeys)
                    .mitigation("Ensure that the headers are unique.").toString());
        }
    }

    private static String unifyFieldName(final String name) {
        return name.trim();
    }

    @Override
    public Map<String, DocumentNode> getKeyValueMap() {
        final Map<String, DocumentNode> map = new HashMap<>();
        for (final Map.Entry<String, String> entrySet : this.fields.entrySet()) {
            map.put(String.valueOf(entrySet.getKey()), convert(entrySet.getKey(), entrySet.getValue()));
        }
        return map;
    }

    @Override
    public DocumentNode get(final String key) {
        final String value = this.fields.get(key);
        if (value == null) {
            throw new NoSuchElementException(
                    "No element with name '" + key + "' found. Valid names are: " + fields.keySet());
        }
        return convert(key, value);
    }

    private DocumentNode convert(final String key, final String value) {
        final CsvValueConverter converter = this.converters.findConverter(key);
        try {
            return converter.convert(value);
        } catch (final RuntimeException exception) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSDF-67").message(
                    "Error converting value {{value}} using converter {{converter|u}} (file {{resource name}}, row {{line number}}, column {{column name}}).")
                    .parameter("value", value, "value to convert") //
                    .parameter("converter", converter, "converter that was used for converting the value")
                    .parameter("resource name", this.resourceName, "resource name or path to the CSV file")
                    .parameter("line number", this.originalLineNumber, "line number of the value (starting with 1)")
                    .parameter("column name", key, "column name of the value") //
                    .mitigation(
                            "Please fix the value in the CSV file or choose a different mapping for converting the value.")
                    .toString(), exception);
        }
    }

    @Override
    public boolean hasKey(final String key) {
        final boolean hasKey = this.fields.containsKey(key);
        if (!hasKey) {
            LOG.warning(() -> ExaError.messageBuilder("W-VSDF-68")
                    .message("Field {{field name}} not found in available fields {{available fields}}.", key,
                            this.fields.keySet())
                    .mitigation("Make sure that field names in CSV file and mapping match.").toString());
        }
        return hasKey;
    }
}
