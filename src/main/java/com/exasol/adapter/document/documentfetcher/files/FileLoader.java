package com.exasol.adapter.document.documentfetcher.files;

import java.util.Iterator;

/**
 * Classes implementing this interface load files from a specific files system type or network storage.
 */
public interface FileLoader {

    /**
     * Get the content of the file as stream.
     * 
     * @return content of the file
     */
    public Iterator<LoadedFile> loadFiles();
}
