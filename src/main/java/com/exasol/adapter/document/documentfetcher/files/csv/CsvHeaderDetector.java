package com.exasol.adapter.document.documentfetcher.files.csv;

import static java.util.stream.Collectors.toList;

import java.util.*;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;

import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.parsers.DataType;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.reading.CsvReader.ResultColumn;

/**
 * This class detects if a CSV file contains a header row or not.
 */
class CsvHeaderDetector {
    private static final Logger LOG = Logger.getLogger(CsvHeaderDetector.class.getName());

    private final RemoteFile remoteFile;

    CsvHeaderDetector(final RemoteFile remoteFile) {
        this.remoteFile = remoteFile;
    }

    boolean hasHeaderRow() {
        final ColumnDataTypes firstRowDataTypes = firstRowDataTypes();
        if (firstRowDataTypes.hasNonStrings()) {
            LOG.finest(() -> "First row contains non string types: " + firstRowDataTypes);
            return false;
        }
        final ColumnDataTypes secondRowDataTypes = secondRowDataTypes();
        if (firstRowDataTypes.equals(secondRowDataTypes)) {
            LOG.finest(() -> "First and second row have same types: " + firstRowDataTypes);
            return false;
        } else {
            LOG.finest(() -> "First and second row have different types: " + firstRowDataTypes + " vs "
                    + secondRowDataTypes);
            return true;
        }
    }

    private ColumnDataTypes firstRowDataTypes() {
        return getDataTypes(csvParserConfigOneRow(0));
    }

    private ColumnDataTypes secondRowDataTypes() {
        return getDataTypes(csvParserConfigOneRow(1));
    }

    private ColumnDataTypes getDataTypes(final CsvSpecs parserConfig) {
        return new ColumnDataTypes(CsvParser.parse(this.remoteFile, parserConfig));
    }

    private CsvSpecs csvParserConfigOneRow(final int skippedRows) {
        return CsvSpecs.builder().hasHeaderRow(false).numRows(1).skipRows(skippedRows).build();
    }

    static final class ColumnDataTypes {
        private static final Set<DataType> TYPES_CONSIDERED_STRING = Set.of(DataType.STRING, DataType.CUSTOM,
                DataType.DATETIME_AS_LONG, DataType.TIMESTAMP_AS_LONG);
        private final List<DataType> types;

        ColumnDataTypes(final CsvReader.Result result) {
            this(Arrays.stream(result.columns()) //
                    .map(ResultColumn::dataType) //
                    .map(type -> consideredAsString(type) ? DataType.STRING : type) //
                    .collect(toList()));
        }

        ColumnDataTypes(final List<DataType> types) {
            this.types = types;
        }

        boolean hasNonStrings() {
            return !isAllStrings();
        }

        boolean isAllStrings() {
            return this.types.stream().allMatch(ColumnDataTypes::consideredAsString);
        }

        private static boolean consideredAsString(final DataType type) {
            return TYPES_CONSIDERED_STRING.contains(type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.types);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ColumnDataTypes other = (ColumnDataTypes) obj;
            return Objects.equals(this.types, other.types);
        }

        @Override
        public String toString() {
            return this.types.toString();
        }
    }
}
