# Virtual Schema for Files

[![Build Status](https://github.com/exasol/virtual-schema-common-document-files/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/virtual-schema-common-document-files/actions/workflows/ci-build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.exasol/virtual-schema-common-document-files)](https://search.maven.org/artifact/com.exasol/virtual-schema-common-document-files)

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

Supported document types:

* JSON
* [JSON-Lines (one json document per line)](https://jsonlines.org/)
* Parquet

You can also [add support for different document types](doc/user_guide/document_type_plugin_development_guide.md).

## Official Dialects:

You cannot directly use this adapter!
Please, use one of the file source-specific dialects from the list below. If this list does not contain your file source you can [implement your own file source](doc/user_guide/dialect_development_guide.md).

* [S3](https://github.com/exasol/s3-document-files-virtual-schema/)
* [Exasol BucketFS](https://github.com/exasol/bucketfs-document-files-virtual-schema/)

## Additional Information

* [User Guide](doc/user_guide/user_guide.md)
* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)