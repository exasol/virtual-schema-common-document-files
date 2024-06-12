package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class ColumnNameConverterTest {

    @Test
    void upperSnakeCaseConverterTest() {
        final ColumnNameConverter columnNameConverter = ColumnNameConverter.upperSnakeCaseConverter();
        assertThat(columnNameConverter.convertColumnName("columnName"), equalTo("COLUMN_NAME"));
    }

    @Test
    void originalNameConverterTest() {
        final ColumnNameConverter columnNameConverter = ColumnNameConverter.originalNameConverter();
        assertThat(columnNameConverter.convertColumnName("columnName"), equalTo("columnName"));
    }
}
