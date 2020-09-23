# User Guides for the generic Virtual Schema for Document Files

You cannot directly use this adapter! Use one of the file source-specific dialects instead. 
For a complete list of all official dialects, check the [README](../../README.md).
If you need something different, you can also [implement a custom dialect](howto_create_new_dialect.md).

This user guide only covers the generic parts of the file virtual schema dialects.
If you want to get started or need data source-specific information, start with the dialect-specific user guide.

# Document Types

This Virtual Schema implements support for different document types (JSON, JSON-Lines, ...).
Don't confuse that with the different data source specific dialects.
Each dialect implements a different file provider (S3, BucketFS, ...). 
With each of these dialects, you define mappings for the different data types listed here.

You can not add different data types by adding a dialect.
In the future, there will be a different approach for that ([issue](https://github.com/exasol/virtual-schema-common-document-files/issues/16)). 
At the moment it is not possible.

This adapter automatically detects the data type from the file extension.
That means that your files must have the correct file extension.

## JSON

File extension: `.json`

Parallelization: Yes 

For JSON data, you typically have each document in a separate file.
So in the Exasol table, you want to have one row per document.
To define the path to such mappings, use the GLOB syntax.
That means that you can use `*` and `?` as wildcards, where `*` matches multiple characters and `?` a single one.
So, for example, if your data files are named `book-1.json`, `book-2.json` and so on, 
you can refer to them as `book-*.json`.

## JSON-Lines

File extension: `.jsonl`

Parallelization: No

JSON-Lines files store one JSON document per line. 
In contrast to using a JSON array, that has the advantage that the file can be parsed line by line.

The JSON-Lines file type does not support parallel loading.