package com.exasol.adapter.document.documentfetcher.files;

/**
 * This class contains methods and constants for calculating {@code INTEGER} and {@code VARCHAR} column sizes.
 */
public class ColumnSizeCalculator {
    private static final double LOG2 = Math.log10(2.0);
    /** Number of digits in a 32 bit integer */
    public static final int INT_32_DIGITS = getNumberOfDigitsForInt(32);
    /** Number of digits in a 64 bit integer */
    public static final int INT_64_DIGITS = getNumberOfDigitsForInt(64);
    /** Maximum length of an Exasol {@code VARCHAR} column */
    public static final int MAX_VARCHAR_COLUMN_SIZE = 2_000_000;

    private ColumnSizeCalculator() {
        // empty on purpose
    }

    /**
     * Calculate the number of digits of an integer.
     *
     * @param numberOfBits number of bits of the integer
     * @return number of digits
     */
    public static int getNumberOfDigitsForInt(final int numberOfBits) {
        return (int) Math.ceil(numberOfBits * LOG2);
    }
}
