package com.exasol.adapter.document.documentfetcher.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.exasol.bucketfs.BucketfsFileFactory;

/**
 * {@link FileLoader} for BucketFS.
 */
public class BucketfsFileLoader implements FileLoader {
    public static final String BUCKETFS_PREFIX = "bucketfs:";
    private final String fileName;

    public BucketfsFileLoader(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream loadFile() {
        try {
            final File file = new BucketfsFileFactory().openFile(this.fileName);
            return new FileInputStream(file);
        } catch (final FileNotFoundException exception) {
            throw new IllegalArgumentException(
                    "Could not open " + this.fileName + " from BucketFS. Cause: " + exception.getMessage(), exception);
        }
    }

    /**
     * Get file name. Used in tests.
     * 
     * @return file name
     */
    String getFileName() {
        return this.fileName;
    }
}
