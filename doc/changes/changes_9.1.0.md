# Virtual Schema for Document Data in Files 9.1.0, released 2026-??-??

Code name: `TIMESTAMP` Precision Support

## Summary

This release adds support for `TIMESTAMP` precision. This means you can use `TIMESTAMP(0)` to `TIMESTAMP(9)` in your EDML mapping for all supported data types CSV, JSON and Parquet.

Please note that JSON files only support timestamps for number fields, interpreting the number as milliseconds since epoch. To enable this conversion, you need to set `notTimestampBehavior=CONVERT_OR_ABORT` for that column in the EDML definition file.

## Features

* #207: Added `TIMESTAMP` precision support for CSV
* #208: Added `TIMESTAMP` precision support tests for JSON Lines

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:12.0.3` to `12.1.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.7.2` to `1.7.3`
* Updated `com.exasol:performance-test-recorder-java:0.1.5` to `0.1.6`
* Updated `com.exasol:virtual-schema-common-document:12.0.3` to `12.1.0`
