package com.exasol.adapter.document.documentfetcher.files.parquet;

import static org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName.INT32;
import static org.apache.parquet.schema.Type.Repetition.REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.parquet.example.data.Group;
import org.apache.parquet.schema.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.documentfetcher.files.LoadedFile;
import com.exasol.adapter.document.documentfetcher.files.LocalLoadedFile;
import com.exasol.adapter.document.documentnode.DocumentBigDecimalValue;
import com.exasol.adapter.document.documentnode.DocumentNode;
import com.exasol.adapter.document.documentnode.avro.AvroRecordNode;

class ParquetDocumentFetcherTest {
    @TempDir
    Path tempDir;

    @Test
    void testReadInt() throws IOException {
        final Path parquetFile = getSingleValueParquetFile("my_value", Types.primitive(INT32, REQUIRED), 123);
        final DocumentBigDecimalValue valueNode = (DocumentBigDecimalValue) runDocumentFetcherAndGetFirstResult(
                parquetFile).get("my_value");
        assertThat(valueNode.getValue(), equalTo(new BigDecimal(123)));
    }

    @Test
    void testReadDecimal() throws IOException {
        final Path parquetFile = getSingleValueParquetFile("my_value",
                Types.primitive(INT32, REQUIRED).as(LogicalTypeAnnotation.decimalType(2, 8)), 123);
        final DocumentBigDecimalValue valueNode = (DocumentBigDecimalValue) runDocumentFetcherAndGetFirstResult(
                parquetFile).get("my_value");
        assertThat(valueNode.getValue(), equalTo(new BigDecimal(123)));
    }

    @Test
    void testReadGroup() throws IOException {
        final Path parquetFile = getListParquetFile();
        runDocumentFetcherAndGetFirstResult(parquetFile).get("numbers");
    }

    private AvroRecordNode runDocumentFetcherAndGetFirstResult(final Path parquetFile) throws IOException {
        final LoadedFile loadedFile = new LocalLoadedFile(parquetFile);
        final List<DocumentNode> result = new ParquetDocumentFetcher(null, null, null).readDocuments(loadedFile)
                .collect(Collectors.toList());
        return (AvroRecordNode) result.get(0);
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