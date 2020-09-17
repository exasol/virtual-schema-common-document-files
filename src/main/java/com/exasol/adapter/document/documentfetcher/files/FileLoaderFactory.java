package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.documentfetcher.files.BucketfsFileLoader.BUCKETFS_PREFIX;

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
     * @param filePattern           files to load
     * @param segmentDescription    segmentation for parallel execution
     * @param connectionInformation connection to the data source
     * @return {@link FileLoader}
     */
    public FileLoader getLoader(final String filePattern, final SegmentDescription segmentDescription,
            final ExaConnectionInformation connectionInformation) {
        final String url = connectionInformation.getAddress();
        if (url.toLowerCase().startsWith(BUCKETFS_PREFIX)) {
            return new BucketfsFileLoader(url.replaceFirst(BUCKETFS_PREFIX, ""), filePattern, segmentDescription);
        } else {
            throw new IllegalArgumentException("Invalid connection string '" + url
                    + "'. It starts with unsupported protocol. Supported protocols are [bucketfs:/<bucketfs>/].");
        }
    }
}
