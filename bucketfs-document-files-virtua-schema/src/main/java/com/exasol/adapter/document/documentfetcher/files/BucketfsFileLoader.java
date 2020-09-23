package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.bucketfs.BucketfsFileFactory;

/**
 * {@link FileLoader} for BucketFS.
 */
class BucketfsFileLoader extends AbstractLocalFileLoader {

    /**
     * Create a new instance of {@link BucketfsFileLoader}.
     *
     * @param baseDirectory      base directory configured in the CONNECTION
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     */
    public BucketfsFileLoader(final String baseDirectory, final String filePattern,
            final SegmentDescription segmentDescription) {
        super(prependBucketFsPrefix(baseDirectory), filePattern, segmentDescription);
    }

    private static String prependBucketFsPrefix(final String path) {
        return new BucketfsFileFactory().openFile(path).toString();
    }
}
