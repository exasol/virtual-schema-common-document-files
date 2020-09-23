package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GlobToRegexConverterTest {

    @CsvSource({ //
            "file-*.json, ^\\Qfile-\\E.*\\Q.json\\E", //
            "file-?.json, ^\\Qfile-\\E.\\Q.json\\E", //
            "file-1.json, ^\\Qfile-1.json\\E"//
    })
    @ParameterizedTest
    void test(final String glob, final String expectedRegex) {
        assertThat(GlobToRegexConverter.convert(glob).toString(), equalTo(expectedRegex));
    }

}