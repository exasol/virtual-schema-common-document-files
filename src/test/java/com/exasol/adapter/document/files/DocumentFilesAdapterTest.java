package com.exasol.adapter.document.files;

import static com.exasol.adapter.capabilities.MainCapability.FILTER_EXPRESSIONS;
import static com.exasol.adapter.capabilities.MainCapability.SELECTLIST_PROJECTION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.spy;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.capabilities.Capabilities;
import com.exasol.adapter.capabilities.LiteralCapability;
import com.exasol.adapter.capabilities.PredicateCapability;
import com.exasol.adapter.document.QueryPlanner;
import com.exasol.adapter.document.mapping.TableKeyFetcher;
import com.exasol.adapter.response.GetCapabilitiesResponse;

class DocumentFilesAdapterTest {

    @Test
    void testGetCapabilities() throws AdapterException {
        final DocumentFilesAdapter adapter = spy(DocumentFilesAdapter.class);
        final GetCapabilitiesResponse capabilitiesResponse = adapter.getCapabilities(null, null);
        final Capabilities capabilities = capabilitiesResponse.getCapabilities();
        assertAll(//
                () -> assertThat(capabilities.getMainCapabilities(),
                        containsInAnyOrder(SELECTLIST_PROJECTION, FILTER_EXPRESSIONS)),
                () -> assertThat(capabilities.getLiteralCapabilities(), containsInAnyOrder(LiteralCapability.STRING)), //
                () -> assertThat(capabilities.getPredicateCapabilities(), containsInAnyOrder(PredicateCapability.EQUAL))//
        );
    }

    @Test
    void testGetQueryPlanner() throws AdapterException {
        final DocumentFilesAdapter adapter = spy(DocumentFilesAdapter.class);
        assertThat(adapter.getQueryPlanner(null), instanceOf(QueryPlanner.class));
    }

    @Test
    void testGetTableKeyFetcher() throws AdapterException, TableKeyFetcher.NoKeyFoundException {
        final DocumentFilesAdapter adapter = spy(DocumentFilesAdapter.class);
        final TableKeyFetcher tableKeyFetcher = adapter.getTableKeyFetcher(null);
        assertThat(tableKeyFetcher.fetchKeyForTable(null, Collections.emptyList()), containsInAnyOrder());
    }
}