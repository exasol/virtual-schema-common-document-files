package com.exasol.adapter.document.documentfetcher.files.csv;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;

/**
 * Helper class for csv configuration method(s)
 */
public class CsvConfigurationHelper {
    private CsvConfigurationHelper(){
        //Add a private constructor to hide the implicit public one.
    }
    /**
     * get a csv configuration from the additionalConfiguration string( which contains a nameless serialised JSON object)
     *
     * @param additionalConfiguration
     * @return CsvConfiguration object
     */
    public static CsvConfiguration getCsvConfiguration(String additionalConfiguration) {
        if (additionalConfiguration != null && !additionalConfiguration.isEmpty()) {
            try (JsonReader jsonReader = Json.createReader(new StringReader(additionalConfiguration))) {
                JsonObject additionalConfigurationJson = jsonReader.readObject();
                boolean hasHeaders = additionalConfigurationJson.getBoolean("csv-headers", false);
                return new CsvConfiguration(hasHeaders);
            }
        }
        return null;
    }
}
