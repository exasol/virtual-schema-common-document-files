# Virtual Schema for Document Data in Files 8.1.0, released 2024-??-??

Code name: Configure column names for automatic schema inference

## Summary

This release allows configuring the mapping of column names for the automatic schema inference in Parquet and CSV files. Before, the virtual schema always converted source column names to `UPPER_SNAKE_CASE` to create the Exasol column names. This is now configurable with configuration option `AUTO_INFERENCE_COLUMN_NAMES`:
* `CONVERT_TO_UPPER_SNAKE_CASE`: convert column names to `UPPER_SNAKE_CASE` (default)
* `KEEP_SOURCE`: use the original column name from the source Parquet/CSV file. Note that this may cause problems when the column name is not a valid Exasol identifier.

**Breaking change:** Nested fields in Parquet files were wrongly always used in their original form. These names are now treated as normal fields, i.e. they are converted to `UPPER_SNAKE_CASE` by default.

## Features

* #163: Added option to keep original column name for auto inference

## Dependency Updates

### Test Dependency Updates

* Removed `org.apache.commons:commons-text:1.12.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.3.3`
