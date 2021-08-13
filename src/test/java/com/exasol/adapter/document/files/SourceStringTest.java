package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SourceStringTest {

    @ParameterizedTest
    @ValueSource(strings = { "test.txt", "test/test.txt", "test.1.txt" })
    void testGetFileTypeWithExtension(final String sourceString) {
        assertThat(new SourceString(sourceString).getFileType(), equalTo("txt"));

    }

    @Test
    void testGetFileTypeWithOverride() {
        assertThat(new SourceString("myFile.txt(import-as:json)").getFileType(), equalTo("json"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "myFile.txt", "myFile.txt(import-as:json)" })
    void testGetPattern(final String sourceString) {
        assertThat(new SourceString(sourceString).getFilePattern(), equalTo("myFile.txt"));
    }

    @Test
    void testNoExtensionException() {
        final SourceString sourceString = new SourceString("test");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, sourceString::getFileType);
        assertThat(exception.getMessage(), startsWith("E-VSDF-15: Invalid file name 'test'."));
    }
}