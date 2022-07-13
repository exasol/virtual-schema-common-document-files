package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.StringRemoteFileContent;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class CsvIterableTest {
    @Test
    void testIterable() {
        final RemoteFile remoteFile = new RemoteFile("", 0,
                new StringRemoteFileContent("{\"id\": \"book-1\"}\n{\"id\": \"book-2\"}"));
        final CsvConfiguration csvConfiguration = new CsvConfiguration(false);
        CsvIterable csvIterable = new CsvIterable(remoteFile, csvConfiguration);
        assertThat(csvIterable.iterator(), instanceOf(CsvIterator.class));
    }
}
