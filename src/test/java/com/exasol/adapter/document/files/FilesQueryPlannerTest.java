package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.exasol.adapter.document.DataLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.adapter.document.querypredicate.NoPredicate;

class FilesQueryPlannerTest {

    @Mock
    private static FileLoaderFactory fileLoaderFactory;
    public static final FilesQueryPlanner FACTORY = new FilesQueryPlanner(fileLoaderFactory);

    @Test
    void testUnsupportedFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.unknown-type");
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> FACTORY.planQuery(remoteTableQuery, 1));
        assertThat(exception.getMessage(), equalTo(
                "Could not find a file type implementation for test.unknown-type. Please check the file extension."));
    }

    @Test
    void testJsonFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json");
        final List<DataLoader> documentFetchers = FACTORY.planQuery(remoteTableQuery, 10).getDataLoaders();
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(10)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonFilesDataLoader.class))//
        );
    }

    @Test
    void testJsonlFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.jsonl");
        final List<DataLoader> documentFetchers = FACTORY.planQuery(remoteTableQuery, 10).getDataLoaders();
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(1)), //
                () -> assertThat(documentFetchers.get(0), instanceOf(JsonFilesDataLoader.class))//
        );
    }

    private RemoteTableQuery getRemoteTableQuery(final String filePattern) {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn(filePattern);
        return new RemoteTableQuery(tableMapping, Collections.emptyList(), new NoPredicate());
    }
}