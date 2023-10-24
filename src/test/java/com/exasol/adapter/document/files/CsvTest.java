package com.exasol.adapter.document.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.csv.NullSinkFactory;

import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.reader.NamedCsvRow;
import io.deephaven.csv.CsvSpecs;
import io.deephaven.csv.parsers.*;
import io.deephaven.csv.reading.CsvReader;
import io.deephaven.csv.sinks.Sink;
import io.deephaven.csv.sinks.SinkFactory;
import io.deephaven.csv.util.CsvReaderException;

class CsvTest {

    private static final Path CSV_FILE = Path.of("/tmp/largefile.csv");

    @Disabled
    @Test
    void test() throws CsvReaderException, IOException {
        final InputStream inputStream = Files.newInputStream(CSV_FILE);
        CsvSpecs specs = CsvSpecs.csv();
        specs = CsvSpecs.builder().skipRows(0).numRows(20).build();
        specs = CsvSpecs.builder() //
                // .skipRows(0).numRows(10000) //
                .putParserForName("Timestamp", DateTimeAsLongParser.INSTANCE) //
                .putParserForName("String", StringParser.INSTANCE) //
                .putParserForName("Bool", BooleanAsByteParser.INSTANCE) //
                .putParserForName("Long0", LongParser.INSTANCE) //
                .putParserForName("Long1", LongParser.INSTANCE) //
                .putParserForName("Double0", DoubleParser.INSTANCE) //
                .putParserForName("Double1", DoubleParser.INSTANCE) //
                .putParserForName("Double2", DoubleParser.INSTANCE) //
                .build();
        SinkFactory sinkFactory = SinkFactory.arrays();
        // sinkFactory = SinkFactory.ofSimple(null, null, null, null, null, null, null, null, null, null, null, null,
        // null, null, null, null, null, null, null, null, null)
        sinkFactory = new NullSinkFactory();
        sinkFactory = simple();
        final Instant start = Instant.now();
        System.out.println("Reading...");
        final CsvReader.Result result = CsvReader.read(specs, inputStream, sinkFactory);
        final long numRows = result.numRows();
        System.out.println("Num rows: " + numRows);
        int columnCount = 0;
        for (final CsvReader.ResultColumn col : result) {
            final Object data = col.data();
            System.out.println("Col " + col.name() + ", type " + col.dataType() + " data: " + data);
            columnCount++;
        }
        System.out.println("Column counter: " + columnCount + " in " + Duration.between(start, Instant.now()));
    }

    private static SinkFactory simple() {
        return SinkFactory.ofSimple(CustomByteSink::new, null, CustomIntSink::new, CustomLongSink::new, null,
                CustomDoubleSink::new, CustomBooleanSink::new, null, CustomStringSink::new, CustomDateTimeSink::new,
                CustomTimestampSink::new);
    }

    private static abstract class CustomAbstractSink<T> implements Sink<T> {
        private final int col;

        public CustomAbstractSink(final int col) {
            this.col = col;
        }

        @Override
        public void write(final T src, final boolean[] isNull, final long destBegin, final long destEnd,
                final boolean appending) {
            if (!appending) {
                throw new IllegalStateException("Overwriting not supported by streaming sink");
            }
            System.out.println(
                    "Process col " + col + " of type " + src.getClass().getName() + " size " + (destEnd - destBegin));
        }

        @Override
        public Object getUnderlying() {
            return null;
        }
    }

    private static class CustomByteSink extends CustomAbstractSink<byte[]> {
        public CustomByteSink(final int col) {
            super(col);
        }
    }

    private static class CustomIntSink extends CustomAbstractSink<int[]> {
        public CustomIntSink(final int col) {
            super(col);
        }
    }

    private static class CustomLongSink extends CustomAbstractSink<long[]> {
        public CustomLongSink(final int col) {
            super(col);
        }
    }

    private static class CustomDoubleSink extends CustomAbstractSink<double[]> {
        public CustomDoubleSink(final int col) {
            super(col);
        }
    }

    private static class CustomBooleanSink extends CustomAbstractSink<byte[]> {
        public CustomBooleanSink(final int col) {
            super(col);
        }
    }

    private static class CustomStringSink extends CustomAbstractSink<String[]> {
        public CustomStringSink(final int col) {
            super(col);
        }
    }

    private static class CustomDateTimeSink extends CustomAbstractSink<long[]> {
        public CustomDateTimeSink(final int col) {
            super(col);
        }
    }

    private static class CustomTimestampSink extends CustomAbstractSink<long[]> {
        public CustomTimestampSink(final int col) {
            super(col);
        }
    }

    @Disabled
    @Test
    void fastCsv() throws IOException {
        System.out.println("Reading with fastcsv...");
        final Instant start = Instant.now();
        int rowCount = 0;
        try (de.siegmar.fastcsv.reader.CsvReader csv = de.siegmar.fastcsv.reader.CsvReader.builder().build(CSV_FILE)) {
            for (final CsvRow csvRow : csv) {
                rowCount++;
            }
        }
        System.out.println("Row count: " + rowCount + " in " + Duration.between(start, Instant.now()));
    }

    @Disabled
    @Test
    void namedFastCsv() throws IOException {
        System.out.println("Reading with named fastcsv...");
        int rowCount = 0;
        final Instant start = Instant.now();
        try (de.siegmar.fastcsv.reader.NamedCsvReader csv = de.siegmar.fastcsv.reader.NamedCsvReader.builder()
                .build(CSV_FILE)) {
            System.out.println("Headers: " + csv.getHeader());
            for (final NamedCsvRow csvRow : csv) {
                rowCount++;
            }
        }
        System.out.println("Row count: " + rowCount + " in " + Duration.between(start, Instant.now()));
    }
}