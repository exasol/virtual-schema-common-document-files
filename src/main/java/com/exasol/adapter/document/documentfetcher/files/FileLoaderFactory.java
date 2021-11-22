package com.exasol.adapter.document.documentfetcher.files;

import java.io.Serializable;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.DocumentFetcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory interface for {@link FileLoader}.
 * 
 * <p>
 * Classes implementing this interface must be serializable since they are shipped along with the
 * {@link DocumentFetcher} to the UDF.
 * </p>
 */
public interface FileLoaderFactory extends Serializable {

    /**
     * Get a {@link FileLoader}.
     *
     * @param filePattern           files to load
     * @param connectionInformation connection to the data source
     * @return {@link FileLoader}
     */
    public FileLoader getLoader(StringFilter filePattern, ExaConnectionInformation connectionInformation);
}
