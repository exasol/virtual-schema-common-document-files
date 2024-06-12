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

### CSV Files

File extension: `.csv`

Parallelization: No

## Mapping Multiple Files

For some file type (for example JSON) each source file contains only a single document. That means, that you have one file for each row in the mapped table. To define mappings for such types, you can use the GLOB syntax.

You can use `*`, `**` and `?` as wildcard characters, where `*` matches multiple characters and `?` a single one. So, for example, if your data files are named `book-1.json`, `book-2.json` and so on, you can refer to them as `book-*.json`.

While `*` and `?` do not match cross directories, `**` also matches recursively into nested directories. For file source, that do not have directory structures (like S3) `*` and `**` have the same behaviour.

## Overriding Auto-detected File Type

The adapter automatically detects the document type by the file extension. If your files don't have the correct extension rename them. If that's not an option for you, you can also explicitly specify the file type:

```
source: "test.my-strange-ending(import-as:json)"
```

## Filtering

The files Virtual Schema allows you to efficiently filter the files to load by using selections `=` or [`LIKE`](https://docs.exasol.com/db/latest/sql_references/predicates/not_like.htm) on the `SOURCE_REFERENCE` column.

Consider the following query:

```sql
SELECT * FROM LOGS WHERE SOURCE_REFERENCE = 'log_files/2022-01-01.parquet'
```

Even so we might have thousands of log files this query will only transfer exactly one dataset. You can also use `LIKE` expressions. For example, you can load all log files of the month `2020-01` by:

```sql
SELECT * FROM LOGS WHERE SOURCE_REFERENCE LIKE 'log_files/2022-01-%.parquet'
``` 

You can also create the patterns dynamically. For example the following query loads all log files of the current month:

```sql
SELECT * FROM LOGS WHERE SOURCE_REFERENCE LIKE 'log_files/' || TO_CHAR(NOW(), 'YYYY-MM') || '-%.parquet'
```

The Virtual Schema can only push down selections on the `SOURCE_REFERENCE` column. If you add more predicates like in the following example, the Virtual Schema will only read all files matching `log_files/2022-01-?.parquet` and apply the `AND SEVERITY = 'warn'` filter afterwards. So the second filter will not reduce the amount of data that needs to be transferred.

```sql
SELECT * FROM LOGS WHERE SOURCE_REFERENCE LIKE 'log_files/2022-01-%.parquet' AND SEVERITY = 'warn'
```

## Automatic Schema Inference

Automatic schema inference can automatically detect the schema of Parquet and CSV files, see the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

You can configure the mapping of source column names to Exasol column names with configuration option `AUTO_INFERENCE_COLUMN_NAMES`:
* `CONVERT_TO_UPPER_SNAKE_CASE`: convert column names to `UPPER_SNAKE_CASE` (default)
* `KEEP_SOURCE`: use the original column name from the source Parquet/CSV file. Note that this may cause problems when the column name is not a valid Exasol identifier.
