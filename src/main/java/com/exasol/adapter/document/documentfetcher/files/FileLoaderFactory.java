package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.documentfetcher.files.BucketfsFileLoader.BUCKETFS_PREFIX;

import java.io.File;
import java.io.IOException;

import com.exasol.ExaConnectionInformation;

/**
 * Factory for {@link FileLoader}
 */
public class FileLoaderFactory {
    private static final FileLoaderFactory INSTANCE = new FileLoaderFactory();

    private FileLoaderFactory() {
        // empty on purpose
    }

    /**
     * Get a singleton instance of {@link FileLoaderFactory}.
     * 
     * @return singleton instance of {@link FileLoaderFactory}
     */
    public static FileLoaderFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get a {@link FileLoader}.
     * 
     * @param fileName              file to load
     * @param connectionInformation connection to the data source
     * @return {@link FileLoader}
     */
    public FileLoader getLoader(final String fileName, final ExaConnectionInformation connectionInformation) {
        final String url = connectionInformation.getAddress();
        if (url.toLowerCase().startsWith(BUCKETFS_PREFIX)) {
            final String filePath = getPathInBucketfs(fileName, url);
            return new BucketfsFileLoader(filePath);
        } else {
            throw new IllegalArgumentException("Invalid connection string '" + url
                    + "'. It starts with unsupported protocol. Supported protocols are [bucketfs:/<bucketfs>/].");
        }
    }

    private String getPathInBucketfs(final String fileName, final String url) {
        final String baseDirectory = url.replaceFirst(BUCKETFS_PREFIX, "");
        final String filePath = baseDirectory + fileName;
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
}
