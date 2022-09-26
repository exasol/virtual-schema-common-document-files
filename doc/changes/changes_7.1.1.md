# Virtual Schema for document data in files 7.1.1, released 2022-09-26

Code name: Fix vulnerabilities in dependencies

## Summary

This release fixes [sonatype-2022-5401](https://ossindex.sonatype.org/vulnerability/sonatype-2022-5401) in reload4j.

## Features

* #110: Fixed vulnerabilities in dependency

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:parquet-io-java:1.3.1` to `1.3.3`
* Updated `de.siegmar:fastcsv:2.1.0` to `2.2.0`
* Updated `jakarta.json:jakarta.json-api:2.1.0` to `2.1.1`

### Test Dependency Updates

* Removed `com.exasol:exasol-test-setup-abstraction-java:0.3.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:performance-test-recorder-java:0.1.0` to `0.1.1`
* Updated `com.exasol:udf-debugging-java:0.6.2` to `0.6.4`
* Added `nl.jqno.equalsverifier:equalsverifier:3.10.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-core:4.6.1` to `4.8.0`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.8.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.8.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M2` to `3.0.0-M1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.11.0` to `2.10.0`
* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`
