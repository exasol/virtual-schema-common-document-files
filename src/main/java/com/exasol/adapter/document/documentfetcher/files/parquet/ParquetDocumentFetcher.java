package com.exasol.adapter.document.documentfetcher.files.parquet;

import java.io.IOException;
import java.util.Iterator;

import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.AfterAllCallbackIterator;
import com.exasol.errorreporting.ExaError;
import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;

/**
 * {@link FileTypeSpecificDocumentFetcher} for parquet files.
 */
public class ParquetDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 4416738850532387816L;

    @Override
    public Iterator<DocumentNode> readDocuments(final RemoteFile remoteFile) {
        final InputFile hadoopInputFile = SeekableInputStreamAdapter.convert(remoteFile.getRandomAccessInputStream());
        try {
            final ParquetReader<Row> reader = RowParquetReader.builder(hadoopInputFile).build();
            return new AfterAllCallbackIterator<>(new ParquetIterator(reader), () -> tryToCloseReader(reader));
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSDF-7").message("Failed to read parquet file.").toString(), exception);
        }
    }

    private void tryToCloseReader(final ParquetReader<Row> reader) {
        try {
            reader.close();
        } catch (final IOException exception) {
            // doesn't matter
        }
    }
}
