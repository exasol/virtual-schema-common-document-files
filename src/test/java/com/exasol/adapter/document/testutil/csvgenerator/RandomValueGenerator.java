package com.exasol.adapter.document.testutil.csvgenerator;

/**
 * Implementors of this interface generate random strings in a custom format, e.g. integers, doubles or timestamps.
 */
interface RandomValueGenerator {
    /**
     * Generate the next random value.
     *
     * @return a new random value
     */
    String nextValue();
}
