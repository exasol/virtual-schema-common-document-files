# Virtual Schema for Files

[![Build Status](https://travis-ci.com/exasol/virtual-schema-common-document-files.svg?branch=master)](https://travis-ci.com/exasol/virtual-schema-common-document-files)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)


This Virtual Schemas allows you to access documents stored in files like any regular Exasol table.

Supported document file formats:

* JSON
* [JSON-Lines (one json document per line)](https://jsonlines.org/) 

Supported Storage drivers:

* [Exasol BucketFS](https://docs.exasol.com/database_concepts/bucketfs/bucketfs.htm)

## Information for Users

* [User guide](doc/user_guide/user_guide.md)

## Dependencies

### Run Time Dependencies

Running the DynamoDB Virtual Schema requires a Java Runtime version 11 or later.

| Dependency                                                                          | Purpose                                                | License                          |
|-------------------------------------------------------------------------------------|--------------------------------------------------------|----------------------------------|
| [Exasol Virtual Schema Common Document][virtual-schema-common-document]             | Common module of Exasol Virtual Schemas adapters       | MIT License                      |

### Test Dependencies

| Dependency                                                                          | Purpose                                                | License                          |
|-------------------------------------------------------------------------------------|--------------------------------------------------------|----------------------------------|
| [Apache Maven](https://maven.apache.org/)                                           | Build tool                                             | Apache License 2.0               |
| [Exasol Testcontainers][exasol-testcontainers]                                      | Exasol extension for the Testcontainers framework      | MIT License                      |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                                  | Checking for conditions in code via matchers           | BSD License                      |
| [JUnit](https://junit.org/junit5)                                                   | Unit testing framework                                 | Eclipse Public License 1.0       |
| [Testcontainers](https://www.testcontainers.org/)                                   | Container-based integration tests                      | MIT License                      |
| [SLF4J](http://www.slf4j.org/)                                                      | Logging facade                                         | MIT License                      |
| [Mockito](http://site.mockito.org/)                                                 | Mocking framework                                      | MIT License                      |
| [Test Database Builder][test-db-builder]                                            | Fluent database interfaces for testing                 | MIT License                      |

### Maven Plug-ins

| Plug-in                                                                             | Purpose                                                | License                          |
|-------------------------------------------------------------------------------------|--------------------------------------------------------|----------------------------------|
| [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)    | Setting required Java version                          | Apache License 2.0               |
| [Maven Exec Plugin](https://www.mojohaus.org/exec-maven-plugin/)                    | Executing external applications                        | Apache License 2.0               |
| [Maven Assembly Plugin](https://maven.apache.org/plugins/maven-assembly-plugin/)    | Creating JAR                                           | Apache License 2.0               |
| [Maven Enforcer Plugin][maven-enforcer-plugin]                                      | Controlling environment constants                      | Apache License 2.0               |
| [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)   | Integration testing                                    | Apache License 2.0               |
| [Maven Jacoco Plugin](https://www.eclemma.org/jacoco/trunk/doc/maven.html)          | Code coverage metering                                 | Eclipse Public License 2.0       |
| [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)   | Unit testing                                           | Apache License 2.0               |
| [Maven Dependency Plugin](https://maven.apache.org/plugins/maven-dependency-plugin/)| Unpacking jacoco agent                                 | Apache License 2.0               |
| [Sonatype OSS Index Maven Plugin][sonatype-oss-index-maven-plugin]                  | Checking Dependencies Vulnerability                    | ASL2                             |
| [Versions Maven Plugin][versions-maven-plugin]                                      | Checking if dependencies updates are available         | Apache License 2.0               |
| [Artifact Reference Checker Plugin][artifact-reference-checker-plugin]              | Check if artifact is referenced with correct version   | MIT License                      |

[exasol-testcontainers]: https://github.com/exasol/exasol-testcontainers
[maven-enforcer-plugin]: http://maven.apache.org/enforcer/maven-enforcer-plugin/
[sonatype-oss-index-maven-plugin]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[versions-maven-plugin]: https://www.mojohaus.org/versions-maven-plugin/
[edml-doc]: https://exasol.github.io/virtual-schema-common-ducument/schema_doc/index.html
[virtual-schema-common-document]: https://github.com/exasol/virtual-schema-common-document
[artifact-reference-checker-plugin]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[test-db-builder]: https://github.com/exasol/test-db-builder-java
