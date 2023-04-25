package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ToUpperSnakeCaseConverterTest {
    @ParameterizedTest
    @CsvSource({ //
            "test, TEST", //
            "myTable, MY_TABLE", //
            "MyTable, MY_TABLE", //
            "myCSV, MY_CSV", //
            "my_table, MY_TABLE", //
            "MY_TABLE, MY_TABLE" })
    void testConversion(final String input, final String expected) {
        assertThat(ToUpperSnakeCaseConverter.toUpperSnakeCase(input), Matchers.equalTo(expected));
    }
}
