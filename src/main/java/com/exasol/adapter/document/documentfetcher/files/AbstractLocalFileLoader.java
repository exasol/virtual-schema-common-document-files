package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Abstract basis for {@link FileLoader}s local files.
 * 
 * @implNote This class is only used by the {@link BucketfsFileLoader}. It is introduced to support unit testing.
 */
abstract class AbstractLocalFileLoader implements FileLoader {
    public static final Pattern NON_GLOB_PATH_PATTERN = Pattern.compile("([^\\*\\?\\[]*\\/).*");
    private final String path;
    private final SegmentMatcher segmentMatcher;

    /**
     * Create a new instance of {@link AbstractLocalFileLoader}.
     * 
     * @param baseDirectory      base directory configured in the CONNECTION
     * @param filePattern        GLOB pattern for the file set to load
     * @param segmentDescription files to load
     */
    public AbstractLocalFileLoader(final String baseDirectory, final String filePattern,
            final SegmentDescription segmentDescription) {
        this.path = concatenatePathsInjectionFree(baseDirectory, filePattern);
        this.segmentMatcher = new SegmentMatcher(segmentDescription);
    }

    private String concatenatePathsInjectionFree(final String baseDirectory, final String fileName) {
        final String filePath = Path.of(baseDirectory, fileName).toString();
        try {
            if (!new File(filePath).getCanonicalPath().startsWith(filePath)) {
                throw new IllegalArgumentException(fileName
                        + " leaves the directory configured in the connection string. For security reasons, this is not allowed. "
                        + "Please change the directory in the connection string.");
            }
            return filePath;
        } catch (final IOException exception) {
            throw new IllegalArgumentException("Invalid file path '" + filePath + "'.");
        }
    }

    @Override
    public Stream<InputStream> loadFiles() {
        try {
            final Matcher matcher = NON_GLOB_PATH_PATTERN.matcher(this.path);
            if (!matcher.matches()) {
                throw new IllegalStateException("Could not find non-glob path");
            }
            final Path nonGlobPath = new File(matcher.group(1)).toPath();
            final PathMatcher globMatcher = FileSystems.getDefault().getPathMatcher("glob:" + this.path);
            return Files.walk(nonGlobPath).filter(globMatcher::matches).filter(this::isFilePartOfThisSegment)
                    .map(this::getInputStream);
        } catch (final IOException exception) {
            throw new IllegalArgumentException(
                    "Could not open " + this.path + " from BucketFS. Cause: " + exception.getMessage(), exception);
        }
    }

    private boolean isFilePartOfThisSegment(final Path path) {
        return this.segmentMatcher.matches(path.toString());
    }

    private InputStream getInputStream(final Path path) {
        try {
            return new FileInputStream(path.toFile());
        } catch (final FileNotFoundException exception) {
            throw new IllegalArgumentException(
                    "Could not open " + this.path + " from BucketFS. Cause: " + exception.getMessage(), exception);
        }
    }

    @Override
    public String getFilePattern() {
        return this.path;
    }
}
