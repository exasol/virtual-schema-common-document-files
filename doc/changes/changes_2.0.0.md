# Virtual Schema for document data in files 2.0.0, released 2021-07-16

Code name: Added Parquet Support

## Summary

In this release we added Parquet support and did performance optimizations.

## Features

* #60: Parquet support

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.2.0` to `0.4.0`
* Added `com.exasol:parquet-io-java:1.0.2`
* Updated `com.exasol:virtual-schema-common-document:3.0.0` to `4.0.0`
* Removed `org.mockito:mockito-core:3.6.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.2.1` to `1.4.0`
* Updated `com.exasol:udf-debugging-java:0.2.0` to `0.4.0`
* Updated `com.exasol:virtual-schema-common-document:3.0.0` to `4.0.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.0` to `5.7.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.0` to `5.7.1`
* Added `org.mockito:mockito-core:3.8.0`
* Added `org.mockito:mockito-junit-jupiter:3.8.0`

### Plugin Dependency Updates

* Removed `com.exasol:artifact-reference-checker-maven-plugin:0.3.1`
* Added `com.exasol:error-code-crawler-maven-plugin:0.5.0`
* Updated `com.exasol:project-keeper-maven-plugin:0.3.0` to `0.10.0`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.13`
* Removed `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
