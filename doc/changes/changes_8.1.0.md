# Virtual Schema for Document Data in Files 8.1.0, released 2024-??-??

Code name: Configure column names for automatic schema inference

## Summary

This release allows configuring the mapping of column names for the automatic schema inference in Parquet and CSV files. Before, the virtual schema always converted source column names to `UPPER_SNAKE_CASE` to create the Exasol column names. This is now configurable with EDML configuration option `autoInferenceColumnNames`. See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

**Breaking change:** Nested fields in Parquet files were wrongly always used in their original form. These names are now treated as normal fields, i.e. they are converted to `UPPER_SNAKE_CASE` by default.

## Features

* #163: Added option to keep original column name for auto inference

## Dependency Updates

### Test Dependency Updates

* Removed `org.apache.commons:commons-text:1.12.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.3.3`
