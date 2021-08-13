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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.adapter.document.querypredicate.*;
import com.exasol.adapter.sql.SqlLiteralString;

@ExtendWith(MockitoExtension.class)
class FilesQueryPlannerTest {

    @Mock
    private static FileLoaderFactory fileLoaderFactory;
    public static final FilesQueryPlanner FACTORY = new FilesQueryPlanner(fileLoaderFactory);

    @Test
    void testUnsupportedFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.unknown-type", new NoPredicate());
        final UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> FACTORY.planQuery(remoteTableQuery, 1));
        assertThat(exception.getMessage(), equalTo(
                "E-VSDF-13: Could not find a file type implementation for '.unknown-type'. Please check the file extension."));
    }

    @Test
    void testJsonFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json", new NoPredicate());
        final FetchQueryPlan queryPlan = (FetchQueryPlan) FACTORY.planQuery(remoteTableQuery, 10);
        final List<DocumentFetcher> documentFetchers = queryPlan.getDocumentFetcher();
        assertAll(//
                () -> assertThat(documentFetchers.size(), equalTo(10)),
                () -> assertThat(documentFetchers, Matchers.hasItem(instanceOf(JsonDocumentFetcher.class)))//
        );
    }

    @Test
    void testJsonlFileType() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.jsonl", new NoPredicate());
        final FetchQueryPlan queryPlan = (FetchQueryPlan) FACTORY.planQuery(remoteTableQuery, 10);
        final List<DocumentFetcher> documentFetchers = queryPlan.getDocumentFetcher();
        assertThat(documentFetchers, Matchers.contains(instanceOf(JsonLinesDocumentFetcher.class)));
    }

    @Test
    void testContradiction() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json",
                new ColumnLiteralComparisonPredicate(AbstractComparisonPredicate.Operator.EQUAL,
                        new SourceReferenceColumnMapping(), new SqlLiteralString("other.json")));
        final QueryPlan queryPlan = FACTORY.planQuery(remoteTableQuery, 10);
        assertThat(queryPlan, instanceOf(EmptyQueryPlan.class));
    }

    @Test
    void testFileTypeOverride() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test.json(import-as:jsonl)", new NoPredicate());
        final FetchQueryPlan queryPlan = (FetchQueryPlan) FACTORY.planQuery(remoteTableQuery, 10);
        final List<DocumentFetcher> documentFetchers = queryPlan.getDocumentFetcher();
        assertThat(documentFetchers, Matchers.contains(instanceOf(JsonLinesDocumentFetcher.class)));
    }

    private RemoteTableQuery getRemoteTableQuery(final String filePattern, final QueryPredicate selection) {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn(filePattern);
        return new RemoteTableQuery(tableMapping, Collections.emptyList(), selection);
    }
}