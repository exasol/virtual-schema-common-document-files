package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AbstractLocalFileLoaderTest {

    @Test
    void testInjection() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileLoaderStub("/buckets/", "../etc/passwd*.json", SegmentDescription.NO_SEGMENTATION));
    }

    @Test
    void testLoadWithGlob(@TempDir final Path tempDir) throws IOException {
        createTestFile(tempDir, "testFile", "file-1");
        createTestFile(tempDir, "testFile", "file-2");
        createTestFile(tempDir, "otherFiles", "file-3");
        final FileLoaderStub fileLoader = new FileLoaderStub(tempDir.toString(), "testFile*.json",
                SegmentDescription.NO_SEGMENTATION);
        final List<InputStream> inputStreams = fileLoader.loadFiles().collect(Collectors.toList());
        final List<String> result = readFirstLineFromStreams(inputStreams);
        assertThat(result, containsInAnyOrder("file-1", "file-2"));
    }

    @NotNull
    private List<String> readFirstLineFromStreams(final List<InputStream> inputStreams) throws IOException {
        final List<String> result = new ArrayList<>(2);
        for (final InputStream inputStream : inputStreams) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result.add(reader.readLine());
            reader.close();
            inputStream.close();
        }
        return result;
    }

    private void createTestFile(final Path tempDir, final String name, final String content) throws IOException {
        final Path testFile = Files.createTempFile(tempDir, name, ".json");
        final FileWriter fileWriter = new FileWriter(testFile.toFile());
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
    }

    private static class FileLoaderStub extends AbstractLocalFileLoader {

        public FileLoaderStub(final String baseDirectory, final String filePattern,
                final SegmentDescription segmentDescription) {
            super(baseDirectory, filePattern, segmentDescription);
        }
    }
}