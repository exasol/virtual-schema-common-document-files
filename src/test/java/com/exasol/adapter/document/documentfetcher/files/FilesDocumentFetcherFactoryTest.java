package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentnode.json.JsonNodeVisitor;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;

class FilesDocumentFetcherFactoryTest {

    public static final FilesDocumentFetcherFactory FACTORY = new FilesDocumentFetcherFactory();

    @Test
    void testUnsupportedFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.unknown-type");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> FACTORY.buildDocumentFetcherForQuery(remoteTableQuery, 1));
        assertThat(exception.getMessage(),
                equalTo("Cannot map this file because it has a unknown type. Supported endings are: [.json, .jsonl]"));
    }

    @Test
    void testJsonFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.json");
        final List<DocumentFetcher<JsonNodeVisitor>> documentFetchers = FACTORY
                .buildDocumentFetcherForQuery(remoteTableQuery, 10);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(10)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonDocumentFetcher.class))//
        );
    }

    @Test
    void testJsonlFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.jsonl");
        final List<DocumentFetcher<JsonNodeVisitor>> documentFetchers = FACTORY
                .buildDocumentFetcherForQuery(remoteTableQuery, 10);
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(1)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonLinesDocumentFetcher.class))//
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