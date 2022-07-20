package com.exasol.adapter.document.documentfetcher.files.csv;

/**
 * Object to hold/pass along csv file configuration
 */
public class CsvConfiguration {
    CsvConfiguration(final boolean headers){
        this.headers = headers;
    }
    private final boolean headers ;

    /**
     * @return boolean that defines if the CSV file contains headers or not
     */
    public boolean getHasHeaders(){
        return headers;
    }
}
