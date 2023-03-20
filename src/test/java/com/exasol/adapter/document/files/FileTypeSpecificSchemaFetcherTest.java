package com.exasol.adapter.document.files;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.edml.Fields;
import com.exasol.adapter.document.edml.MappingDefinition;
import com.exasol.adapter.document.files.FileTypeSpecificSchemaFetcher.SingleFileSchemaFetcher;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;

@ExtendWith(MockitoExtension.class)
class FileTypeSpecificSchemaFetcherTest {

    @Mock
    SingleFileSchemaFetcher delegateMock;
    @Mock
    RemoteFileFinder fileFinderMock;
    @Mock
    RemoteFile file1Mock;
    @Mock
    RemoteFile file2Mock;

    @Test
    void unsupported() {
        assertTrue(FileTypeSpecificSchemaFetcher.unsupported().fetchSchema(null).isEmpty());
    }

    @Test
    void singleFileFetcherFailsForMissingFile() {
        simulateFiles();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, this::fetchSingleFileSchema);
        assertThat(exception.getMessage(),
                startsWith("E-VSDF-57: Error detecting mapping because no file matches the SOURCE expression."));
    }

    @Test
    void singleFileFetcherForSingleFile() {
        simulateFiles(this.file1Mock);
        final MappingDefinition mapping = Fields.builder().build();
        when(this.delegateMock.fetchSchema(same(this.file1Mock))).thenReturn(mapping);
        assertThat(fetchSingleFileSchema().get(), sameInstance(mapping));
    }

    @Test
    void singleFileFetcherForMultipleFiles() {
        simulateFiles(this.file1Mock, this.file2Mock);
        final MappingDefinition mapping = Fields.builder().build();
        when(this.delegateMock.fetchSchema(same(this.file1Mock))).thenReturn(mapping);
        assertThat(fetchSingleFileSchema().get(), sameInstance(mapping));
    }

    private void simulateFiles(final RemoteFile... files) {
        when(this.fileFinderMock.loadFiles()).thenReturn(new CloseableIteratorWrapper<>(asList(files).iterator()));
    }

    private Optional<MappingDefinition> fetchSingleFileSchema() {
        return FileTypeSpecificSchemaFetcher.singleFile(this.delegateMock).fetchSchema(this.fileFinderMock);
    }
}
