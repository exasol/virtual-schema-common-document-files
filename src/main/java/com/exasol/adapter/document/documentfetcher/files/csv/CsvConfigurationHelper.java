package com.exasol.adapter.document.documentfetcher.files.csv;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;

public class CsvConfigurationHelper {
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
