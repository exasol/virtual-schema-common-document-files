package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.files.DocumentFilesAdapter;

public class BucketfsDocumentFilesAdapter extends DocumentFilesAdapter {
    @Override
    protected FileLoaderFactory getFileLoaderFactory() {
        return null;
    }
}
