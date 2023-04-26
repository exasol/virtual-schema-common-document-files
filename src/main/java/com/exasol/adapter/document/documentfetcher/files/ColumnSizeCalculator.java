package com.exasol.adapter.document.documentfetcher.files;

/**
 * This class contains methods and constants for calculating {@code INTEGER} and {@code VARCHAR} column sizes.
 */
public class ColumnSizeCalculator {
    public static final int INT_32_DIGITS = getNumberOfDigitsForInt(32);
    public static final int INT_64_DIGITS = getNumberOfDigitsForInt(64);
    public static final int MAX_VARCHAR_COLUMN_SIZE = 2_000_000;

    private ColumnSizeCalculator() {
        // empty on purpose
    }

    public static int getNumberOfDigitsForInt(final int numberOfBits) {
        return (int) Math.ceil(Math.log10(Math.pow(2, numberOfBits)));
    }
}
