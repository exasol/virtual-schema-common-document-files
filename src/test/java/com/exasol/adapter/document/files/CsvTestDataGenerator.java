package com.exasol.adapter.document.files;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.csv.CsvTestSetup;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.AbstractToColumnMapping.AbstractToColumnMappingBuilder;

class CsvTestDataGenerator {
    private static final Logger LOGGER = Logger.getLogger(CsvTestDataGenerator.class.getName());

    private final List<ColumnGenerator> columns;
    private final long rowCount;
    private final int fileCount;
    private final Path tempDir;

    private CsvTestDataGenerator(final long rowCount, final int fileCount, final List<ColumnGenerator> columns,
            final Path tempDir) {
        this.rowCount = rowCount;
        this.fileCount = fileCount;
        this.columns = columns;
        this.tempDir = tempDir;
    }

    public long getRowCount() {
        return this.rowCount;
    }

    public int getFileCount() {
        return this.fileCount;
    }

    Fields getMapping() {
        final Fields.FieldsBuilder fieldsBuilder = Fields.builder();
        for (final ColumnGenerator col : this.columns) {
            fieldsBuilder.mapField(col.getColumnName(), col.mapping);
        }
        return fieldsBuilder.build();
    }

    List<Path> writeFiles() {
        LOGGER.info("Generating " + this.fileCount + " CSV files using " + this + "...");
        final Instant start = Instant.now();
        final List<Path> files = new ArrayList<>(this.fileCount);
        for (int i = 0; i < this.fileCount; i++) {
            files.add(writeFile());
        }
        LOGGER.info("Generated " + this.fileCount + " files in " + Duration.between(start, Instant.now()));
        return files;
    }

    private Path writeFile() {
        final List<String> columnsNames = this.columns.stream() //
                .map(ColumnGenerator::getColumnName) //
                .collect(toList());
        try (final CsvTestSetup csvTestSetup = new CsvTestSetup(this.tempDir, columnsNames)) {
            for (long rowCounter = 0; rowCounter < this.rowCount; rowCounter++) {
                final List<String> row = this.columns.stream() //
                        .map(ColumnGenerator::generateData) //
                        .collect(toList());
                csvTestSetup.writeRow(row);
            }
            return csvTestSetup.getCsvFile();
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed writing CSV file", exception);
        }
    }

    @Override
    public String toString() {
        return "CsvTestDataGenerator [columnCount=" + this.columns.size() + ", rowCount=" + this.rowCount
                + ", fileCount=" + this.fileCount + "]";
    }

    static Builder builder() {
        return new Builder();
    }

    private static class ColumnGenerator {
        private final int columnIndex;
        private final MappingDefinition mapping;
        private final Supplier<String> dataGenerator;
        private final String typeHint;

        ColumnGenerator(final int columnIndex, final String typeHint,
                final AbstractToColumnMappingBuilder<?, ?> mappingBuilder, final Supplier<String> dataGenerator) {
            this.columnIndex = columnIndex;
            this.typeHint = typeHint;
            this.mapping = mappingBuilder.destinationName(getColumnName().toUpperCase()).build();
            this.dataGenerator = dataGenerator;
        }

        String getColumnName() {
            return "col_" + this.columnIndex + "_" + this.typeHint;
        }

        public String generateData() {
            return this.dataGenerator.get();
        }
    }

    static class Builder {
        private static final int DATA_TYPE_COUNT = 6;

        final Random random = new Random(1);
        int stringLength = -1;
        long rowCount = -1;
        int columnCount = -1;
        int fileCount = -1;
        Path tempDir;

        public Builder tempDir(final Path tempDir) {
            this.tempDir = tempDir;
            return this;
        }

        Builder stringLength(final int stringLength) {
            this.stringLength = stringLength;
            return this;
        }

        Builder rowCount(final long rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        Builder columnCount(final int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        Builder fileCount(final int fileCount) {
            this.fileCount = fileCount;
            return this;
        }

        CsvTestDataGenerator build() {
            validate();
            return new CsvTestDataGenerator(this.rowCount, this.fileCount, generateColumns(), this.tempDir);
        }

        private void validate() {
            if (this.stringLength <= 0) {
                throw new IllegalArgumentException("Illegal value for string length: " + this.stringLength);
            }
            if (this.rowCount <= 0) {
                throw new IllegalArgumentException("Illegal value for row count: " + this.rowCount);
            }
            if (this.columnCount <= 0) {
                throw new IllegalArgumentException("Illegal value for column count " + this.columnCount);
            }
            if (this.fileCount <= 0) {
                throw new IllegalArgumentException("Illegal value for file count " + this.fileCount);
            }
            if (this.tempDir == null) {
                throw new IllegalArgumentException("Temp dir not defined");
            }
        }

        private List<ColumnGenerator> generateColumns() {
            final List<ColumnGenerator> columns = new ArrayList<>(this.columnCount);
            for (int col = 0; col < this.columnCount; col++) {
                final int col1 = col;
                columns.add(createColumnWithCyclingType(col1, this.random));
            }
            return columns;
        }

        private ColumnGenerator createColumnWithCyclingType(final int col, final Random random) {
            final int dataType = col % DATA_TYPE_COUNT;
            switch (dataType) {
            case 0:
                return new ColumnGenerator(col, "str", ToVarcharMapping.builder().varcharColumnSize(2_000_000),
                        new RandomString(this.stringLength, random));
            case 1:
                return new ColumnGenerator(col, "bool", ToBoolMapping.builder(), new RandomBoolean(random));
            case 2:
                return new ColumnGenerator(col, "int", ToDecimalMapping.builder(), new RandomInteger(random));
            case 3:
                return new ColumnGenerator(col, "double", ToDoubleMapping.builder(), new RandomDouble(random));
            case 4:
                return new ColumnGenerator(col, "date", ToDateMapping.builder(), new RandomDate(random));
            case 5:
                return new ColumnGenerator(col, "timestamp", ToTimestampMapping.builder(), new RandomTimestamp(random));
            default:
                throw new IllegalStateException("Illegal data type " + dataType + " value for column " + col);
            }
        }
    }

    static class RandomBoolean implements Supplier<String> {
        private final Random random;

        RandomBoolean(final Random random) {
            this.random = random;
        }

        @Override
        public String get() {
            return String.valueOf(this.random.nextBoolean());
        }
    }

    static class RandomInteger implements Supplier<String> {
        private final Random random;

        RandomInteger(final Random random) {
            this.random = random;
        }

        @Override
        public String get() {
            return String.valueOf(this.random.nextInt());
        }
    }

    static class RandomDouble implements Supplier<String> {
        private final Random random;

        RandomDouble(final Random random) {
            this.random = random;
        }

        @Override
        public String get() {
            return String.valueOf(this.random.nextDouble());
        }
    }

    static class RandomDate implements Supplier<String> {
        private final Random random;

        RandomDate(final Random random) {
            this.random = random;
        }

        @Override
        public String get() {
            final Instant randomInstant = Instant.ofEpochSecond(this.random.nextInt());
            final LocalDate randomDate = LocalDate.ofInstant(randomInstant, ZoneId.of("UTC"));
            return randomDate.toString();
        }
    }

    static class RandomTimestamp implements Supplier<String> {
        private final Random random;

        RandomTimestamp(final Random random) {
            this.random = random;
        }

        @Override
        public String get() {
            final Instant randomInstant = Instant.ofEpochSecond(this.random.nextInt());
            return Timestamp.from(randomInstant).toString();
        }
    }

    /**
     * Generates random alphanumeric strings.
     *
     * Adapted from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     */
    static class RandomString implements Supplier<String> {
        static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        static final String lower = upper.toLowerCase(Locale.ROOT);
        static final String digits = "0123456789";
        static final String alphanum = upper + lower + digits;
        private final Random random;
        private final char[] symbols;
        private final char[] buf;

        private RandomString(final int length, final Random random, final String symbols) {
            if (length < 1) {
                throw new IllegalArgumentException();
            }
            if (symbols.length() < 2) {
                throw new IllegalArgumentException();
            }
            this.random = Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }

        RandomString(final int length, final Random random) {
            this(length, random, alphanum);
        }

        @Override
        public String get() {
            for (int idx = 0; idx < this.buf.length; ++idx) {
                this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
            }
            return new String(this.buf);
        }
    }
}
