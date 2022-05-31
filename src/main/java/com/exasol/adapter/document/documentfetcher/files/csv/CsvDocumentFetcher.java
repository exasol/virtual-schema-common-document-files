package com.exasol.adapter.document.documentfetcher.files.csv;

import com.exasol.adapter.document.documentfetcher.files.InputDataException;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.DocumentNodeVisitor;
import com.exasol.adapter.document.documentnode.csv.CsvNodeFactory;
import com.exasol.adapter.document.documentnode.csv.CsvObjectNode;
import com.exasol.adapter.document.files.FileTypeSpecificDocumentFetcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;
import com.exasol.adapter.document.iterators.TransformingIterator;
import com.exasol.errorreporting.ExaError;
import com.opencsv.exceptions.CsvException;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {@link FileTypeSpecificDocumentFetcher} for CSV files.
 */
public class CsvDocumentFetcher implements FileTypeSpecificDocumentFetcher {
    private static final long serialVersionUID = 2783593249946168796L;
    //private static final CsvReaderFactory CSV_READER_FACTORY = Csv.createReaderFactory(null);

    @Override
    public CloseableIterator<DocumentNode> readDocuments(final FileSegment segment) {
        if (!segment.getSegmentDescription().equals(FileSegmentDescription.ENTIRE_FILE)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSDF-17")
                    .message("The CsvDocumentFetcher does not support loading split files.").ticketMitigation()
                    .toString());
        }
        final RemoteFile remoteFile = segment.getFile();
            //documentation on readDocuments
            //https://github.com/exasol/virtual-schema-common-document-files/blob/main/doc/user_guide/document_type_plugin_development_guide.md#the-documentfetcher
            //List csvNodesList = new ArrayList<DocumentNode>();
        return new CsvIterator(segment.getFile());
//
//            final InputStream inputStream = remoteFile.getContent().getInputStream();
//            final CsvReader csvReader = buildCsvReader(inputStream);
//            return new  TransformingIterator<>(new CloseableIteratorWrapper<>(csvReader.iterator(),()-> {csvReader.close();inputStream.close();}),
//                    row -> new CsvObjectNode(row));
    }

    @Override
    public boolean supportsFileSplitting() {
        return false;
    }

    //todo: add support for custom settings
    private CsvReader buildCsvReader(final InputStream inputStream) {
            //todo: can I do a string conversion here?
            return CsvReader.builder().build(inputStream.toString());
    }
}
