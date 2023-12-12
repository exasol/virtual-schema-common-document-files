# Virtual Schema for Document Data in Files 8.0.0, released 2023-12-13

Code name: Support Exasol 8

## Summary

This release upgrades to `virtual-schema-common-document:10.1.0` which adds support for Exasol 8. This brings the following changes for virtual schemas:

### Remove support for `TIMESTAMP WITH LOCAL TIME ZONE`

This release adds support for Exasol 8 by removing support for data type `TIMESTAMP WITH LOCAL TIME ZONE`. This type caused problems with the stricter type checks enabled by default in Exasol, causing pushdown queries for document based virtual schemas to fail with the following error:

```
Data type mismatch in column number 5 (1-indexed).Expected TIMESTAMP(3) WITH LOCAL TIME ZONE, but got TIMESTAMP(3).
```

We fixed this error by removing support `TIMESTAMP WITH LOCAL TIME ZONE` completely. This is a breaking change, so we updated the version to 8.0.0.

###  Support `ALTER VIRTUAL SCHEMA SET`

This release adds support for `ALTER VIRTUAL SCHEMA SET`. This will allow changing properties like `MAPPING` of document based virtual schemas without dropping and re-creating the virtual schema:

```sql
-- Update EDML mapping of the virtual schema
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET MAPPING = '...';

-- Enable remote logging or change the log level
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET DEBUG_ADDRESS = 'host:3000' LOG_LEVEL = 'FINEST';
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET LOG_LEVEL = 'INFO';
```

See the [documentation for `ALTER SCHEMA`](https://docs.exasol.com/db/latest/sql/alter_schema.htm) for details.

## Features

* #148: Added support for Exasol version 8

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:9.4.3` to `10.1.0`
* Updated `io.deephaven:deephaven-csv:0.12.0` to `0.14.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.6.1` to `1.6.3`
* Updated `com.exasol:virtual-schema-common-document:9.4.3` to `10.1.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.2` to `3.15.4`
* Updated `org.apache.commons:commons-text:1.10.0` to `1.11.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-junit-jupiter:5.6.0` to `5.8.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.13` to `2.9.17`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
