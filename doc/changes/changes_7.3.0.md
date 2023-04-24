# Virtual Schema for Document Data in Files 7.3.0, released 2023-??-??

Code name: CSV Data Types

## Summary

This release adds support for other data types than VARCHAR in CSV files, see the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #125: Added support for data types in CSV files

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.2` to `2.0.3`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.3` to `1.6.0`
* Updated `org.mockito:mockito-junit-jupiter:5.3.0` to `5.3.1`
