package com.exasol.adapter.document.files;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvTestDataGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void testGenerateWritesCsvFile() throws IOException {
        final CsvTestDataGenerator generator = CsvTestDataGenerator.builder() //
                .columnCount(7) //
                .rowCount(10) //
                .stringLength(13) //
                .fileCount(1) //
                .tempDir(this.tempDir) //
                .build();
        final List<String> lines = readGeneratedLines(generator);
        assertAll( //
                () -> assertThat(lines.size(), equalTo(10 + 1)),
                () -> assertThat(lines.get(0),
                        equalTo("col_0_str,col_1_bool,col_2_int,col_3_double,col_4_date,col_5_timestamp,col_6_str")),
                () -> assertThat(lines.get(1), hasLength(96)),
                () -> assertThat(lines.get(1), not(equalTo(lines.get(2)))));
    }

    private List<String> readGeneratedLines(final CsvTestDataGenerator generator) throws IOException {
        final Path file = generator.writeFiles().get(0);
        assertTrue(Files.exists(file), "CSV file exists");
        final String content = Files.readString(file);
        return asList(content.split("\\r?\\n|\\r"));
    }

    @Test
    void testGenerateCreatesMapping() throws IOException {
        final CsvTestDataGenerator generator = CsvTestDataGenerator.builder() //
                .columnCount(5) //
                .rowCount(10) //
                .stringLength(13) //
                .fileCount(1) //
                .tempDir(this.tempDir) //
                .build();
        assertThat(generator.getMapping().getFieldsMap(), aMapWithSize(5));
    }
}
