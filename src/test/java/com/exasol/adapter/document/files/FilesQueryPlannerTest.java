package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;
import com.exasol.adapter.document.mapping.SourceReferenceColumnMapping;
import com.exasol.adapter.document.mapping.TableMapping;
import com.exasol.adapter.document.queryplan.*;
import com.exasol.adapter.document.queryplanning.RemoteTableQuery;
import com.exasol.adapter.document.querypredicate.*;
import com.exasol.adapter.sql.SqlLiteralString;

@ExtendWith(MockitoExtension.class)
class FilesQueryPlannerTest {

    private FilesQueryPlanner mockQueryPlanner(final RemoteFileFinder loader) {
        final FileFinderFactory fileFinderFactory = mock(FileFinderFactory.class);
        when(fileFinderFactory.getFinder(any(), any())).thenAnswer(i -> loader);
        return new FilesQueryPlanner(fileFinderFactory, mock(ConnectionPropertiesReader.class));
    }

    private RemoteFileFinder mockLoaderThatReturnFiles() {
        final RemoteFileFinder loader = mock(RemoteFileFinder.class);
        when(loader.loadFiles())
                .thenAnswer(i -> new CloseableIteratorWrapper<>(List.of(mock(RemoteFile.class)).iterator()));
        return loader;
    }

    private RemoteFileFinder mockLoaderThatReturnNoFiles() {
        final RemoteFileFinder loader = mock(RemoteFileFinder.class);
        when(loader.loadFiles()).thenAnswer(i -> new CloseableIteratorWrapper<>(Collections.emptyIterator()));
        return loader;
    }

    @Test
    void testBuildDocumentFetcher() {
        final RemoteFileFinder loader = mockLoaderThatReturnFiles();
        final FilesQueryPlanner queryPlanner = mockQueryPlanner(loader);
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json", new NoPredicate());
        final QueryPlan queryPlan = queryPlanner.planQuery(remoteTableQuery, 10);
        assertThat(queryPlan, instanceOf(FetchQueryPlan.class));
    }

    @Test
    void testBuildDocumentFetcherForEmptyResult() {
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json", new NoPredicate());
        var remoteQuerySelectionToString = remoteTableQuery.getSelection().toString();
        assertThat(remoteQuerySelectionToString, equalTo("NoPredicate"));

        final RemoteFileFinder loader = mockLoaderThatReturnNoFiles();
        final FilesQueryPlanner queryPlanner = mockQueryPlanner(loader);
        final SourceString sourceString = queryPlanner.getSourceString(remoteTableQuery);
        assertThat(sourceString.getFilePattern(), equalTo("test-*.json"));

        final FilesSelectionExtractor.Result splitSelection = queryPlanner.getSplitSelection(sourceString, remoteTableQuery);
        assertThat(queryPlanner.getDocumentFetchers(remoteTableQuery, 10, sourceString, splitSelection).isEmpty(),
                equalTo(true));

        String emptyDocumentFetchersLogMessage = "No document fetchers created for file pattern 'test-*.json' with selection 'NoPredicate'. Returning EmptyQueryPlan.";
        assertThat(queryPlanner.getEmptyDocumentFetchersLogMessage(sourceString, remoteTableQuery),
                equalTo(emptyDocumentFetchersLogMessage));

        final QueryPlan queryPlan = queryPlanner.planQuery(remoteTableQuery, 10);
        assertThat(queryPlan, instanceOf(EmptyQueryPlan.class));
    }

    @Test
    void testContradiction() {
        var sqlLiteral = new SqlLiteralString("other.json");
        var sqlLiteralToString = sqlLiteral.toString();
        final RemoteTableQuery remoteTableQuery = getRemoteTableQuery("test-*.json",
                new ColumnLiteralComparisonPredicate(AbstractComparisonPredicate.Operator.EQUAL,
                        new SourceReferenceColumnMapping(), sqlLiteral));

        var remoteQuerySelectionToString = remoteTableQuery.getSelection().toString();
        assertThat(remoteQuerySelectionToString,
                equalTo(String.format("SOURCE_REFERENCE=%s", sqlLiteralToString)));

        final var queryPlanner = new FilesQueryPlanner(null, mock(ConnectionPropertiesReader.class));
        final SourceString sourceString = queryPlanner.getSourceString(remoteTableQuery);
        assertThat(sourceString.getFilePattern(), equalTo("test-*.json"));

        final FilesSelectionExtractor.Result splitSelection = queryPlanner.getSplitSelection(sourceString, remoteTableQuery);
        assertThat(splitSelection.getSourceFilter().hasContradiction(), equalTo(true));

        String sourceFilterContradictionLogMessage = String.format(
                "Contradiction detected in source filter for file pattern 'test-*.json' with selection 'SOURCE_REFERENCE=%s'. Returning EmptyQueryPlan.",
                sqlLiteralToString);
        assertThat(queryPlanner.getSourceFilterContradictionLogMessage(sourceString, remoteTableQuery),
                equalTo(sourceFilterContradictionLogMessage));
        final QueryPlan queryPlan = queryPlanner.planQuery(remoteTableQuery, 10);
        assertThat(queryPlan, instanceOf(EmptyQueryPlan.class));
    }

    private RemoteTableQuery getRemoteTableQuery(final String filePattern, final QueryPredicate selection) {
        final TableMapping tableMapping = mock(TableMapping.class);
        when(tableMapping.getRemoteName()).thenReturn(filePattern);
        return new RemoteTableQuery(tableMapping, Collections.emptyList(), selection);
    }
}
