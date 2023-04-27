# Virtual Schema for Document Data in Files 7.3.0, released 2023-??-??

Code name: CSV Data Types

## Summary

This release adds auto inference of column data types and support for data types other than `VARCHAR` in CSV files, e.g. `BOOLEAN`, `DECIMAL`, `DOUBLE PRECISION`, and `TIMESTAMP`), see the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #125: Added support for data types in CSV files
* #128: Trimmed CSV header names
* #130: Added schema inference for CSV files without headers
* #131: Added schema inference for CSV files with headers

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.2` to `2.0.3`
* Updated `com.exasol:virtual-schema-common-document:9.3.0` to `9.4.0`
* Added `io.deephaven:deephaven-csv:0.10.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.3` to `1.6.0`
* Updated `com.exasol:virtual-schema-common-document:9.3.0` to `9.4.0`
* Updated `org.mockito:mockito-junit-jupiter:5.3.0` to `5.3.1`
