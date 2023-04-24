package com.exasol.adapter.document.documentfetcher.files.parquet;

import static com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegmentDescription.ENTIRE_FILE;
import static com.exasol.adapter.document.documentnode.util.DocumentNodeMatchers.decimalNode;
import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.documentfetcher.files.LocalRemoteFileContent;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.segmentation.FileSegment;
import com.exasol.adapter.document.documentnode.*;
import com.exasol.adapter.document.documentnode.parquet.RowRecordNode;

class ParquetDocumentFetcherTest {
    @TempDir
    Path tempDir;

    @Test
    void testReadInt() throws IOException {
        final Path parquetFile = getSingleValueParquetFile("my_value", Types.primitive(INT32, REQUIRED), 123);
        final DocumentDecimalValue valueNode = (DocumentDecimalValue) runDocumentFetcherAndGetFirstResult(parquetFile)
                .get("my_value");
        assertThat(valueNode, decimalNode(123));
    }

    @Test
    void testReadDecimal() throws IOException {
        final Path parquetFile = getSingleValueParquetFile("my_value",
                Types.primitive(INT32, REQUIRED).as(LogicalTypeAnnotation.decimalType(2, 8)), 123);
        final DocumentNode valueNode = runDocumentFetcherAndGetFirstResult(parquetFile).get("my_value");
        assertThat(valueNode, decimalNode(1.23));
    }

    @Test
    void testReadGroup() throws IOException {
        final Path parquetFile = getListParquetFile();
        final DocumentArray array = (DocumentArray) runDocumentFetcherAndGetFirstResult(parquetFile).get("numbers");
        assertThat(array.getValuesList(), contains(decimalNode(1), decimalNode(2)));
    }

    private RowRecordNode runDocumentFetcherAndGetFirstResult(final Path parquetFile) {
        final RemoteFile remoteFile = new RemoteFile("", 0, new LocalRemoteFileContent(parquetFile));
        return (RowRecordNode) new ParquetDocumentFetcher().readDocuments(new FileSegment(remoteFile, ENTIRE_FILE))
                .next();
    }

    private Path getListParquetFile() throws IOException {
        final Type integerType = Types.primitive(INT32, REQUIRED).named("element");
        final GroupType groupType = ConversionPatterns.listOfElements(REQUIRED, "numbers", integerType);
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, groupType);
        parquetTestSetup.writeRow(row -> {
            final Group numbers = row.addGroup("numbers");
            numbers.addGroup(0).append("element", 1);
            numbers.addGroup(0).append("element", 2);
        }).closeWriter();
        return parquetTestSetup.getParquetFile();
    }

    private Path getSingleValueParquetFile(final String key, final Types.PrimitiveBuilder<PrimitiveType> typeBuilder,
            final int value) throws IOException {
        final ParquetTestSetup parquetTestSetup = new ParquetTestSetup(this.tempDir, typeBuilder.named(key));
        parquetTestSetup.writeRow(row -> row.add(key, value)).closeWriter();
        return parquetTestSetup.getParquetFile();
    }
}