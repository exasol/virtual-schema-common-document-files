package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.ExaConnectionInformation;

/**
 * Factory for {@link FileLoader}
 */
public class BucketFsFileLoaderFactory implements FileLoaderFactory {
    private static final FileLoaderFactory INSTANCE = new BucketFsFileLoaderFactory();

    private BucketFsFileLoaderFactory() {
        // empty on purpose
    }

    /**
     * Get a singleton instance of {@link BucketFsFileLoaderFactory}.
     * 
     * @return singleton instance of {@link BucketFsFileLoaderFactory}
     */
    public static FileLoaderFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public FileLoader getLoader(final String filePattern, final SegmentDescription segmentDescription,
            final ExaConnectionInformation connectionInformation) {
        final String url = connectionInformation.getAddress();
        return new BucketfsFileLoader(url, filePattern, segmentDescription);
    }
}
