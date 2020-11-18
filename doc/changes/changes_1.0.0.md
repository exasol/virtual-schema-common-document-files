# virtual-schema-common-document-files 1.0.0, released 2020-11-18
 
Code name: Selection on file name
 
## Summary

This release adds the possibility to filter the fetched files using a SOURCE_REFERENCE column and by that improve query performance.

Known issues:

* Certain virtual-schema queries can cause a database crash. For details see [#41](https://github.com/exasol/virtual-schema-common-document-files/issues/41).

## Features / Enhancements

* #22: Added a column containing the file name
* #32: Selection on SOURCE_REFERENCE column
* #34: Selection using LIKE on SOURCE_REFERENCE column
* #38: Removed unnecessary dependencies
* #40: Created abstract integration test
* #36: Added unified error codes 


# Documentation

* #37: Updated development resources

## Dependency updates

* Removed `com.exasol:test-db-builder-java`
* Removed `com.exasol:exasol-testcontainers`
* Removed `org.testcontainers:junit-jupiter`
* Updated `com.exasol:project-keeper-maven-plugin` from 0.1.0 to 0.3.0
* Removed `org.slf4j:slf4j-jdk14`
* Added `com.exasol:hamcrest-resultset-matcher` 1.2.1
* Added `com.exasol:udf-debugging-java` 0.2.0
* Added `com.exasol:error-reporting-java` 0.1.1
* Removed `org.junit.platform:junit-platform-runner`
* Updated `com.exasol:error-reporting-java` from 0.1.1 to 0.2.0
* Updated `org.mockito:mockito-core` from 3.5.13 to 3.6.0
* Updated `org.junit.jupiter:junit-jupiter-params` from 5.6.2 to 5.7.0
* Updated `org.junit.jupiter:junit-jupiter-engine` from 5.6.2 to 5.7.0
