package com.exasol.adapter.document.testutil.csvgenerator;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.csv.CsvTestSetup;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.AbstractToColumnMapping.AbstractToColumnMappingBuilder;

/**
 * This class generates random CSV files as test data, including matching EDML {@link Fields} definition. Use the
 * {@link #builder()} to configure and create a new instance.
 */
public class CsvTestDataGenerator {
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

    /**
     * Get the EDML {@link Fields} definition.
     *
     * @return EDML {@link Fields} definition
     */
    public Fields getMapping() {
        final Fields.FieldsBuilder fieldsBuilder = Fields.builder();
        for (final ColumnGenerator col : this.columns) {
            fieldsBuilder.mapField(col.getColumnName(), col.mapping);
        }
        return fieldsBuilder.build();
    }

    /**
     * Write random CSV files to the given temp folder.
     *
     * @return the generated files
     */
    public List<Path> writeFiles() {
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

    /**
     * Create a new {@link Builder} for {@link CsvTestDataGenerator}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link CsvTestDataGenerator}.
     */
    public static class Builder {
        private static final int DATA_TYPE_COUNT = 6;
        private final RandomValueGeneratorFactory randomFactory = new RandomValueGeneratorFactory(new Random(1));
        private int stringLength = -1;
        private long rowCount = -1;
        private int columnCount = -1;
        private int fileCount = -1;
        private Path tempDir;

        /**
         * @param tempDir the directory where to store the generated files
         * @return {@code this} for fluent programming
         */
        public Builder tempDir(final Path tempDir) {
            this.tempDir = tempDir;
            return this;
        }

        /**
         * @param stringLength the length of the generated string column values
         * @return {@code this} for fluent programming
         */
        public Builder stringLength(final int stringLength) {
            this.stringLength = stringLength;
            return this;
        }

        /**
         * @param rowCount the number of rows per CSV file to generate
         * @return {@code this} for fluent programming
         */
        public Builder rowCount(final long rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        /**
         * @param columnCount the number of columns per CSV file to generate
         * @return {@code this} for fluent programming
         */
        public Builder columnCount(final int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        /**
         * @param fileCount the number of CSV files to generate
         * @return {@code this} for fluent programming
         */
        public Builder fileCount(final int fileCount) {
            this.fileCount = fileCount;
            return this;
        }

        /**
         * @return a new {@link CsvTestDataGenerator}
         */
        public CsvTestDataGenerator build() {
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
                columns.add(createColumnWithCyclingType(col1));
            }
            return columns;
        }

        private ColumnGenerator createColumnWithCyclingType(final int col) {
            final int dataType = col % DATA_TYPE_COUNT;
            switch (dataType) {
            case 0:
                return new ColumnGenerator(col, "str", ToVarcharMapping.builder().varcharColumnSize(this.stringLength),
                        this.randomFactory.createRandomString(this.stringLength));
            case 1:
                return new ColumnGenerator(col, "bool", ToBoolMapping.builder(),
                        this.randomFactory.createRandomBoolean());
            case 2:
                return new ColumnGenerator(col, "int", ToDecimalMapping.builder(),
                        this.randomFactory.createRandomInteger());
            case 3:
                return new ColumnGenerator(col, "double", ToDoubleMapping.builder(),
                        this.randomFactory.createRandomDouble());
            case 4:
                return new ColumnGenerator(col, "date", ToDateMapping.builder(), this.randomFactory.createRandomDate());
            case 5:
                return new ColumnGenerator(col, "timestamp", ToTimestampMapping.builder(),
                        this.randomFactory.createRandomTimestamp());
            default:
                throw new IllegalStateException("Illegal data type " + dataType + " value for column " + col);
            }
        }
    }

    private static class ColumnGenerator {
        final int columnIndex;
        final MappingDefinition mapping;
        final RandomValueGenerator dataGenerator;
        final String typeHint;

        ColumnGenerator(final int columnIndex, final String typeHint,
                final AbstractToColumnMappingBuilder<?, ?> mappingBuilder, final RandomValueGenerator dataGenerator) {
            this.columnIndex = columnIndex;
            this.typeHint = typeHint;
            this.mapping = mappingBuilder.destinationName(getColumnName().toUpperCase()).build();
            this.dataGenerator = dataGenerator;
        }

        String getColumnName() {
            return "col_" + this.columnIndex + "_" + this.typeHint;
        }

        String generateData() {
            return this.dataGenerator.nextValue();
        }
    }
}
