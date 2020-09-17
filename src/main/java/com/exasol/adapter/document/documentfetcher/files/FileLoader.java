package com.exasol.adapter.document.documentfetcher.files;

import java.io.InputStream;
import java.util.stream.Stream;

/**
 * Classes implementing this interface load files from a specific files system type or network storage.
 */
public interface FileLoader {

    /**
     * Get the content of the file as stream.
     * 
     * @return content of the file
     */
    public Stream<InputStream> loadFiles();

    /**
     * Get the path / pattern of the files to load.
     * 
     * @return path / pattern of the files to load
     */
    public String getFilePattern();
}
