package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.files.ConnectionInfoMockFactory.mockConnectionInfoWithAddress;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.adapter.document.querypredicate.*;
import com.exasol.adapter.sql.SqlLiteralString;

@ExtendWith(MockitoExtension.class)
class FilesQueryPlannerTest {
    public static FilesQueryPlanner queryPlanner;

    @BeforeAll
    static void beforeAll() {
        final FileLoader loader = mock(FileLoader.class);
        when(loader.loadFiles()).thenAnswer(I -> Collections.emptyIterator());
        final FileLoaderFactory fileLoaderFactory = mock(FileLoaderFactory.class);
        when(fileLoaderFactory.getLoader(any(), any(), any())).thenAnswer(I -> loader);
        queryPlanner = new FilesQueryPlanner(fileLoaderFactory, mockConnectionInfoWithAddress(""));
    }

    @Test
    void testBuildDocumentFetcher() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json", new NoPredicate());
        final FetchQueryPlan queryPlan = (FetchQueryPlan) queryPlanner.planQuery(remoteTableQuery, 10);
        final List<DocumentFetcher> documentFetchers = queryPlan.getDocumentFetcher();
        assertThat(documentFetchers.size(), equalTo(10));
    }

    @Test
    void testContradiction() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json",
                new ColumnLiteralComparisonPredicate(AbstractComparisonPredicate.Operator.EQUAL,
                        new SourceReferenceColumnMapping(), new SqlLiteralString("other.json")));
        final QueryPlan queryPlan = queryPlanner.planQuery(remoteTableQuery, 10);
        assertThat(queryPlan, instanceOf(EmptyQueryPlan.class));
    }

    private RemoteTableQuery getRemoteTableQuery(final String filePattern, final QueryPredicate selection) {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn(filePattern);
        return new RemoteTableQuery(tableMapping, Collections.emptyList(), selection);
    }
}