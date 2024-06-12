package com.exasol.adapter.document.files;

import static com.exasol.adapter.capabilities.MainCapability.FILTER_EXPRESSIONS;
import static com.exasol.adapter.capabilities.MainCapability.SELECTLIST_PROJECTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.capabilities.*;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.mapping.TableKeyFetcher;
import com.exasol.adapter.document.mapping.auto.ColumnNameConverter;
import com.exasol.adapter.document.mapping.auto.InferredMappingDefinition;

@ExtendWith(MockitoExtension.class)
class DocumentFilesAdapterTest {

    @Mock
    FileFinderFactory fileFinderFactoryMock;
    @Mock
    RemoteFileFinder fileFinderMock;
    @Mock
    ColumnNameConverter columnNameConverterMock;

    @Test
    void testGetCapabilities() {
        final Capabilities capabilities = testee().getCapabilities();
        assertAll(//
                () -> assertThat(capabilities.getMainCapabilities(),
                        containsInAnyOrder(SELECTLIST_PROJECTION, FILTER_EXPRESSIONS)),
                () -> assertThat(capabilities.getLiteralCapabilities(), containsInAnyOrder(LiteralCapability.STRING)), //
                () -> assertThat(capabilities.getPredicateCapabilities(),
                        containsInAnyOrder(PredicateCapability.EQUAL, PredicateCapability.LIKE, PredicateCapability.AND,
                                PredicateCapability.OR, PredicateCapability.NOT))//
        );
    }

    @Test
    void testGetQueryPlanner() {
        assertThat(testee().getQueryPlanner(null, new AdapterProperties(Collections.emptyMap())),
                instanceOf(QueryPlanner.class));
    }

    @Test
    void testGetTableKeyFetcher() throws TableKeyFetcher.NoKeyFoundException {
        final TableKeyFetcher tableKeyFetcher = testee().getTableKeyFetcher(null);
        assertThat(tableKeyFetcher.fetchKeyForTable(null, Collections.emptyList()), containsInAnyOrder());
    }

    @Test
    void testGetSchemaFetcherUnsupported() {
        when(this.fileFinderFactoryMock.getFinder(any(), any())).thenReturn(this.fileFinderMock);
        final DocumentFilesAdapter adapter = testee();
        final Optional<InferredMappingDefinition> result = adapter.getSchemaFetcher(null).fetchSchema("source.json",
                columnNameConverterMock);
        assertThat(result.isEmpty(), is(true));
    }

    private DocumentFilesAdapter testee() {
        return new DocumentFilesAdapter("adapterName", this.fileFinderFactoryMock);
    }
}
