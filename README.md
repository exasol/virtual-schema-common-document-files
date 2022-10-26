# Virtual Schema for Files

[![Build Status](https://github.com/exasol/virtual-schema-common-document-files/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/virtual-schema-common-document-files/actions/workflows/ci-build.yml)
[![Maven Central – Virtual Schema for document data in files](https://img.shields.io/maven-central/v/com.exasol/virtual-schema-common-document-files)](https://search.maven.org/artifact/com.exasol/virtual-schema-common-document-files)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-document-files&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-document-files)

Virtual Schema Common Document Files allows you to query data stored in a document file in the same way as if the data was stored in a regular Exasol database table.

This module is part of a larger project called [Virtual Schemas](https://github.com/exasol/virtual-schemas) covering document based dialects as well as JDBC based, see complete list of [dialects](https://github.com/exasol/virtual-schemas/blob/main/doc/user-guide/dialects.md).

Document-based virtual schemas are characterized by
* a *storage* that is basically a container hosting the document files and also defining the access control and type of account needed to access the files and
* a *document type* defining the format of the document containing the data.

## Storage Variants

You cannot directly use this adapter. Please, use one of the dialects for specific storage variants below.

* [AWS S3](https://github.com/exasol/s3-document-files-virtual-schema/)
* [Exasol BucketFS](https://github.com/exasol/bucketfs-document-files-virtual-schema/)
* [Google Cloud Storage](https://github.com/exasol/google-cloud-storage-document-files-virtual-schema)
* [Azure Blob Storage](https://github.com/exasol/azure-blob-storage-document-files-virtual-schema)
* [Azure Data Lake Storage Gen2](https://github.com/exasol/azure-data-lake-storage-gen2-document-files-virtual-schema)

If this list does not contain your file source you can [implement your own file source](doc/user_guide/dialect_development_guide.md).

## Document Types

Each storage variant can contain documents using any of the following supported document types:
* JSON
* [JSON-Lines (one json document per line)](https://jsonlines.org/)
* Parquet
* CSV

You can also [add support for other document types](doc/user_guide/document_type_plugin_development_guide.md).

## Additional Information

* [User Guide](doc/user_guide/user_guide.md)
* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)
