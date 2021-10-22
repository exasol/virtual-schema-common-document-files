# Virtual Schema for document data in files 2.2.1, released 2021-10-22

Code name: Dependency Updates on New Mapping Types

## Features

* #73: Improved parquet regression tests

## Bug Fixes

#75: Fixed unstable test(`testFilterWithOrOnSourceReferenceWithBugfixForSPOT11018`)

## Refactoring

* #71: Replaced javax.json by jakarta.json

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:6.1.0` to `6.1.1`
* Added `jakarta.json:jakarta.json-api:2.0.1`

### Runtime Dependency Updates

* Added `org.glassfish:jakarta.json:2.0.1`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.0` to `1.5.1`
* Updated `com.exasol:udf-debugging-java:0.4.0` to `0.4.1`
* Updated `com.exasol:virtual-schema-common-document:6.1.0` to `6.1.1`
* Updated `org.mockito:mockito-core:3.12.4` to `4.0.0`
* Updated `org.mockito:mockito-junit-jupiter:3.12.4` to `4.0.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.5.0` to `0.6.0`
* Updated `com.exasol:project-keeper-maven-plugin:1.2.0` to `1.3.1`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.2.0` to `3.3.1`
