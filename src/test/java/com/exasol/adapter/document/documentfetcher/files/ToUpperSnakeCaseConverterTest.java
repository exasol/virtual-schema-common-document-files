package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ToUpperSnakeCaseConverterTest {
    @ParameterizedTest(name = "Name ''{0}'' converted to ''{1}''")
    @CsvSource({ //
            "'',''", //
            "' ','_'", //
            "test, TEST", //
            "myTable, MY_TABLE", //
            "mytable, MYTABLE", //
            "MyTable, MY_TABLE", //
            "myCSV, MY_CSV", //
            "MyCSV, MY_CSV", //
            "my_table, MY_TABLE", //
            "MY_TABLE, MY_TABLE", //
            "my column, MY_COLUMN" })
    void testConversion(final String input, final String expected) {
        assertThat(ToUpperSnakeCaseConverter.toUpperSnakeCase(input), Matchers.equalTo(expected));
    }
}
