package com.exasol.adapter.document.documentfetcher.files;

import java.io.Serializable;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
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
     * @param filePattern              files to load
     * @param connectionPropertyReader reader for connection properties like remote database credentials
     * @return {@link FileLoader}
     */
    public FileLoader getLoader(StringFilter filePattern, final ConnectionPropertiesReader connectionPropertyReader);

    /**
     * Get the URL of the file type specific user guide.
     *
     * @return user guide URL
     */
    public String getUserGuideUrl();
}
