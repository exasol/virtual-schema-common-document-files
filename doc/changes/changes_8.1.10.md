# Virtual Schema for Document Data in Files 8.1.10, released 2025-06-24

Code name: Improve query plan logging

## Summary

This release adds more logging to `buildExplicitSegmentation()` and `buildHashSegmentation` methods of the class `FilesDocumentFetcherFactory`. This allows improving logging around the paths that lead to the creation of the query plan.

## Features

* #194: Add more logging around the paths that lead to the creation of the query plan.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:11.0.3` to `11.0.4`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:11.0.3` to `11.0.4`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.1.0` to `5.2.2`
* Added `org.sonatype.central:central-publishing-maven-plugin:0.7.0`
* Removed `org.sonatype.plugins:nexus-staging-maven-plugin:1.7.0`
