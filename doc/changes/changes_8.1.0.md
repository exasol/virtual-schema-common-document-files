# Virtual Schema for Document Data in Files 8.1.0, released 2024-06-14

Code name: Configure column names for automatic mapping inference

## Summary

This release allows configuring the mapping of column names for the automatic mapping inference in Parquet and CSV files. Before, the virtual schema always converted source column names to `UPPER_SNAKE_CASE` to create the Exasol column names. This is now configurable with EDML property `autoInferenceColumnNames`. This property supports the following values:
* `CONVERT_TO_UPPER_SNAKE_CASE`: Convert column names to `UPPER_SNAKE_CASE` (default).
* `KEEP_ORIGINAL_NAME`: Do not convert column names, use column name from source.

 See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #163: Added option to keep original column name for auto inference

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:10.1.2` to `11.0.0`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:10.1.2` to `11.0.0`
* Added `com.jparams:to-string-verifier:1.4.8`
* Removed `org.apache.commons:commons-text:1.12.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.3.3`
