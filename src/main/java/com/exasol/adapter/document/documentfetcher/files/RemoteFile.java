package com.exasol.adapter.document.documentfetcher.files;

import java.util.Objects;

/**
 * This class describes a file loaded by the {@link RemoteFileFinder}.
 */

public class RemoteFile {

    private final String resourceName;
    private final long size;
    private RemoteFileContent content;

    /**
     * Create a new {@link RemoteFile}.
     *
     * @param resourceName file name
     * @param size         file size in bytes
     * @param content      file content
     */
    public RemoteFile(final String resourceName, final long size, final RemoteFileContent content) {
        this.resourceName = resourceName;
        this.size = size;
        this.content = content;
    }

    /**
     * Get the name of the file.
     *
     * @return name of the file
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * Get the file size in bytes.
     *
     * @return file size in bytes
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Get the file content.
     *
     * @return file content
     */
    public RemoteFileContent getContent() {
        return this.content;
    }

    /**
     * Set the file content.
     *
     * @param content file content
     * @return {@code this} for fluent method calls.
     */
    public RemoteFile withContent(final RemoteFileContent content) {
        this.content = content;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.content, this.resourceName, this.size);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RemoteFile other = (RemoteFile) obj;
        return Objects.equals(this.content, other.content) && Objects.equals(this.resourceName, other.resourceName)
                && (this.size == other.size);
    }
}
