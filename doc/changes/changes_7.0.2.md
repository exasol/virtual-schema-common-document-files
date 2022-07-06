# Virtual Schema for document data in files 7.0.2, released 2022-06-09

Code name: 7.0.2: Upgrade dependencies on 7.0.1

## Summary

This release fixes vulnerabilities in dependencies by updating compile dependency.

## Features

* #104: Upgraded dependencies to fix vulnerabilities

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.3.0` to `1.3.1`
* Removed `com.exasol:performance-test-recorder-java:0.1.0`

### Test Dependency Updates

* Added `com.exasol:exasol-test-setup-abstraction-java:0.3.2`
* Added `com.exasol:performance-test-recorder-java:0.1.0`
* Updated `org.mockito:mockito-core:4.5.1` to `4.6.1`
* Updated `org.mockito:mockito-junit-jupiter:4.5.1` to `4.6.1`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.3.2` to `2.4.6`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.11.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.8`
