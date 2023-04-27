package com.exasol.adapter.document.documentfetcher.files.csv;

import java.io.IOException;
import java.io.InputStream;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.errorreporting.ExaError;

import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.parsers.Parsers;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.util.CsvReaderException;

class CsvParser {

    private CsvParser() {
        // Not instantiable
    }

    static CsvReader.Result parse(final RemoteFile remoteFile, final boolean hasHeaderRow, final long lookAhead) {
        final CsvSpecs parserConfiguration = configuration(hasHeaderRow, lookAhead);
        return parse(remoteFile, parserConfiguration);
    }

    static CsvReader.Result parse(final RemoteFile remoteFile, final CsvSpecs parserConfiguration) {
        try (InputStream inputStream = remoteFile.getContent().getInputStream()) {
            return CsvReader.read(parserConfiguration, inputStream, new NullSinkFactory());
        } catch (final IOException | CsvReaderException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-70")
                            .message("Failed to read CSV file {{file path}}", remoteFile.getResourceName()).toString(),
                    exception);
        }
    }

    private static CsvSpecs configuration(final boolean hasHeaderRow, final long lookAhead) {
        return CsvSpecs.builder() //
                .hasHeaderRow(hasHeaderRow) //
                .parsers(Parsers.DEFAULT) //
                .numRows(lookAhead) //
                .build();
    }
}
