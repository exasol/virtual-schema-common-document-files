package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.StringReader;

import jakarta.json.*;

/**
 * Helper class for csv configuration method(s)
 */
public class CsvConfigurationHelper {
    private CsvConfigurationHelper() {
        // Add a private constructor to hide the implicit public one.
    }

    /**
     * Get a CSV configuration from the additionalConfiguration string (which contains a nameless serialised JSON
     * object)
     *
     * @param additionalConfiguration additionalConfiguration string (which contains a nameless serialised JSON object)
     * @return csv configuration or {@code null} if the given string was null or empty
     */
    public static CsvConfiguration getCsvConfiguration(final String additionalConfiguration) {
        if (additionalConfiguration != null && !additionalConfiguration.isEmpty()) {
            try (JsonReader jsonReader = Json.createReader(new StringReader(additionalConfiguration))) {
                final JsonObject additionalConfigurationJson = jsonReader.readObject();
                final boolean hasHeaders = additionalConfigurationJson.getBoolean("csv-headers", false);
                return new CsvConfiguration(hasHeaders);
            }
        }
        return null;
    }
}
