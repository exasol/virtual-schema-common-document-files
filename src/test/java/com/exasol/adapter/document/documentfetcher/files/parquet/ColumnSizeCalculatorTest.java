package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColumnSizeCalculatorTest {

    @ParameterizedTest
    @CsvSource({ "0, 0", "1, 1", "2, 1", "3, 1", "4, 2", "5, 2", "9, 3", "10, 4", "16, 5", "32, 10", "64, 20",
            "1234567890, 2147483647" })
    void getNumberOfDigitsForInt(final int numberOfBits, final int expectedNumberOfDigits) {
        assertThat(ColumnSizeCalculator.getNumberOfDigitsForInt(numberOfBits), equalTo(expectedNumberOfDigits));
    }
}
