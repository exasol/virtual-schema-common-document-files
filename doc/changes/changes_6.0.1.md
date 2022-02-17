# Virtual Schema for document data in files 6.0.1, released 2022-02-17

Code name: Performance improvement for loading few files

## Summary

In this release we improved the performance when loading only a few files.

## Refactoring

* Added regression tests for #91

## Documentation

* #94: Documented how a query can filter on SOURCE_REFERENCE column

## Bug Fixes

* #91: Removed overhead when loading only a few files on a big cluster

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:8.1.0` to `8.1.1`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:8.1.0` to `8.1.1`
* Updated `org.mockito:mockito-core:4.2.0` to `4.3.1`
* Updated `org.mockito:mockito-junit-jupiter:4.2.0` to `4.3.1`
