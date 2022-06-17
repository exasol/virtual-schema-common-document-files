package com.exasol.adapter.document.documentfetcher.files.csv;

public class CsvConfiguration {
    CsvConfiguration(final boolean headers){
        this.headers = headers;
    }
    private final boolean headers ;
    public boolean getHeaders(){
        return headers;
    }
}
