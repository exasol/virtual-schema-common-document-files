# User Guides for the Generic Document Files Virtual Schema

You cannot directly use this adapter! Use one of the file source-specific dialects instead. For a complete list of all official dialects, check the [README](../../README.md). If the document source you need is not listed there, you can also [implement a custom dialect](dialect_development_guide.md).

This user guide only covers the generic parts of the file virtual schema dialects. If you want to get started or need data source-specific information, start with the dialect-specific user guide.

## Document Types

This Virtual Schema implements support for different document types (JSON, JSON-Lines, ...). Don't mix it up with the data source specific dialects. Each dialect implements a different file provider (S3, BucketFS, ...). With each of these dialects, you define mappings for the different data types listed here.

You can also [add support for different document types](document_type_plugin_development_guide.md).

This adapter automatically detects the data type from the file extension. That means that your files must have the correct file extension.

### JSON

File extension: `.json`

Parallelization: Yes

### JSON-Lines

File extension: `.jsonl`

Parallelization: No

JSON-Lines files store one JSON document per line. In contrast to using a JSON array, that has the advantage that the file can be parsed line by line.

### Parquet Files

File extension: `.parquet`

Parallelization: Yes

## Mapping Multiple Files

For some file type (for example JSON) each source file contains only a single document. That means, that you have one file for each row in the mapped table. To define mappings for such types, you can use the GLOB syntax.

You can use `*`, `**` and `?` as wildcard characters, where `*` matches multiple characters and `?` a single one. So, for example, if your data files are named `book-1.json`, `book-2.json` and so on, you can refer to them as `book-*.json`.

While `*` and `?` do not match cross directories, `**` also matches recursively into nested directories. For file source, that do not have directory structures (like S3 ) `*` and `**` have the same behaviour.

## Known Issues:

* Certain virtual-schema queries can cause a database crash. For details see [#41](https://github.com/exasol/virtual-schema-common-document-files/issues/41).