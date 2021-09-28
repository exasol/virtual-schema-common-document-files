# Virtual Schema for document data in files 2.2.0, released 2021-09-28

Code name: New Mapping Types

## Summary

This release integrates the new features from the virtual-schema-common-document 6.1.0:

> In this release we added the following new mapping types:
>
> * `toDoubleMapping`
> * `toBoolMapping`
> * `toDateMapping`
> * `toTimestampMapping`
>
> In order to use the new features, please update you EDML definitions to version `1.3.0` (no breaking changes).

For details see the [changelog of the virtual-schema-common-document](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/changes/changes_6.1.0.md).

## Features

* #96: Added more tests for parquet types

## Refactoring

* #67: Moved performance-test-recorder to its own repo

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.0.2` to `1.1.0`
* Added `com.exasol:performance-test-recorder-java:0.1.0`
* Updated `com.exasol:virtual-schema-common-document:6.0.0` to `6.1.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.4.0` to `1.5.0`
* Updated `com.exasol:virtual-schema-common-document:6.0.0` to `6.1.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.5` to `0.8.7`
* Updated `org.jacoco:org.jacoco.core:0.8.5` to `0.8.7`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.1` to `5.8.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.1` to `5.8.1`
* Updated `org.mockito:mockito-core:3.8.0` to `3.12.4`
* Updated `org.mockito:mockito-junit-jupiter:3.8.0` to `3.12.4`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:0.10.0` to `1.2.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.5` to `0.8.7`
