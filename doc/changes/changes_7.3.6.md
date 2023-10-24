# Virtual Schema for Document Data in Files 7.3.6, released 2023-10-24

Code name: Validate EDML for duplicate table names

## Summary

This release upgrades to [virtual-schema-common-document 9.4.3](https://github.com/exasol/virtual-schema-common-document/releases/tag/9.4.3). This new version validates that the given EDML mapping uses unique values for each destinationTable entry as duplicate values led to unexpected behavior.

## Features

* #146: Added validation for duplicate destinationTable entries

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:9.4.2` to `9.4.3`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:9.4.2` to `9.4.3`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.12` to `2.9.13`
* Removed `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
