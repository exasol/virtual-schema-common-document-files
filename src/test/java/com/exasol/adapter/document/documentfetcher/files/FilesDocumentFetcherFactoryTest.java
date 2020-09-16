package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

class FilesDocumentFetcherFactoryTest {

    @Test
    void testUnsupportedFileType() {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn("test.unknown-type");
        final RemoteTableQuery remoteTableQuery = mock(RemoteTableQuery.class);
        when(remoteTableQuery.getFromTable()).thenReturn(tableMapping);
        final FilesDocumentFetcherFactory documentFetcherFactory = new FilesDocumentFetcherFactory();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> documentFetcherFactory.buildDocumentFetcherForQuery(remoteTableQuery, 1));
        assertThat(exception.getMessage(),
                equalTo("Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]"));
    }
}