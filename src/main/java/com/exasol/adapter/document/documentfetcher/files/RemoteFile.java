package com.exasol.adapter.document.documentfetcher.files;

import lombok.Data;
import lombok.With;

/**
 * This class describes a file loaded by the {@link FileLoader}.
 */
@Data
public class RemoteFile {
    /** Get the name of the file. */
    private final String resourceName;
    /** File size in bytes. */
    private final long size;
    /** File content. */
    @With
    private final RemoteFileContent content;
}
