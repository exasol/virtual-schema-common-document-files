package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.documentfetcher.files.SegmentDescription.NO_SEGMENTATION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.exasol.ExaConnectionInformation;

class FileLoaderFactoryTest {

    @Test
    void testGetBucketFsLoader() {
        final BucketfsFileLoader loader = (BucketfsFileLoader) FileLoaderFactory.getInstance().getLoader("test.json",
                NO_SEGMENTATION, new ExaConnectionStub("bucketfs:/bfsdefault/default/"));
        assertThat(loader.getFilePattern(), equalTo("/buckets/bfsdefault/default/test.json"));
    }

    @Test
    void testUnknownProtocol() {
        final FileLoaderFactory fileLoaderFactory = FileLoaderFactory.getInstance();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> fileLoaderFactory.getLoader("test.json", NO_SEGMENTATION, new ExaConnectionStub("unknown:/tmp")));
        assertThat(exception.getMessage(), equalTo(
                "Invalid connection string 'unknown:/tmp'. It starts with unsupported protocol. Supported protocols are [bucketfs:/<bucketfs>/]."));
    }

    private static class ExaConnectionStub implements ExaConnectionInformation {

        private final String address;

        private ExaConnectionStub(final String address) {
            this.address = address;
        }

        @Override
        public ConnectionType getType() {
            return ConnectionType.PASSWORD;
        }

        @Override
        public String getAddress() {
            return this.address;
        }

        @Override
        public String getUser() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }
    }
}