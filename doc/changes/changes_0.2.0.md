# virtual-schema-common-document-files 0.2.0, released 2020-10-01
 
Code name: Extracted file backends
 
## Summary

The dependency update of virtual-schema-common-document cause the following API change:

* The UDF definition changed. See the dialects user-guides.

## Features / Enhancements

* #14: Extracted file backends as separate repositories
* #16: Made document types extensible using service loader
* #13: Improved error message on syntax errors in input data

## Bug fixes:

* #12: Fixed error message for empty json files

## Refactoring:

* #23: Added abstract document fetcher
* #25: Added abstract data loader factory

## Documentation

* #17: Added User Guide
* #20: Added documentation for document type plugin development

## Dependency updates

* Updated `com.exasol:virtual-schema-common-document` from 1.0.0 to 2.0.0 
* Updated `org.mockito:mockito-core` from 3.5.10 to 3.5.13
* Updated `com.exasol:exasol-testcontainers` from 3.0.0 to 3.2.0
