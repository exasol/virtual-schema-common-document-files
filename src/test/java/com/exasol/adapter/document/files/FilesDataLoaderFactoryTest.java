package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

class FilesDataLoaderFactoryTest {

    @Mock
    private static FileLoaderFactory fileLoaderFactory;
    public static final FilesDataLoaderFactory FACTORY = new FilesDataLoaderFactory(fileLoaderFactory);

    @Test
    void testUnsupportedFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.unknown-type");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> FACTORY.buildDataLoaderForQuery(remoteTableQuery, 1));
        assertThat(exception.getMessage(),
                equalTo("Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]"));
    }

    @Test
    void testJsonFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.json");
        final List<DataLoader> documentFetchers = FACTORY.buildDataLoaderForQuery(remoteTableQuery, 10);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(10)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonDataLoader.class))//
        );
    }

    @Test
    void testJsonlFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.jsonl");
        final List<DataLoader> documentFetchers = FACTORY.buildDataLoaderForQuery(remoteTableQuery, 10);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(1)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonDataLoader.class))//
        );
    }

    private RemoteTableQuery getRemoteTableQuery(final String filePattern) {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn(filePattern);
        final RemoteTableQuery remoteTableQuery = mock(RemoteTableQuery.class);
        when(remoteTableQuery.getFromTable()).thenReturn(tableMapping);
        return remoteTableQuery;
    }
}