package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColumnSizeCalculatorTest {

    @ParameterizedTest
    @CsvSource({ "0, 0", "1, 1", "2, 1", "3, 1", "4, 2", "5, 2", "9, 3", "10, 4", "16, 5", "32, 10", "64, 20",
            "100, 31", "200, 61" })
    void getNumberOfDigitsForInt(final int numberOfBits, final int expectedNumberOfDigits) {
        assertThat(ColumnSizeCalculator.getNumberOfDigitsForInt(numberOfBits), equalTo(expectedNumberOfDigits));
    }

    @Test
    void constantInt32Digits() {
        assertThat(ColumnSizeCalculator.INT_32_DIGITS, equalTo(10));
    }

    @Test
    void constantInt64Digits() {
        assertThat(ColumnSizeCalculator.INT_64_DIGITS, equalTo(20));
    }
}
