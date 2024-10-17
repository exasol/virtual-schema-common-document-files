# Virtual Schema for Document Data in Files 8.1.4, released 2024-10-17

Code name: Fix CVE-2024-47561 in dependency

## Summary

This release fixes vulnerability CVE-2024-47561 by updating transitive dependency `org.apache.avro:avro` via `com.exasol:parquet-io-java`.

## Security Issues

* #171: Fixed vulnerability CVE-2024-47561 in org.apache.avro:avro
## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.10` to `2.0.11`
* Updated `de.siegmar:fastcsv:3.3.0` to `3.3.1`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.2` to `3.17.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.0` to `5.11.2`
* Updated `org.mockito:mockito-junit-jupiter:5.13.0` to `5.14.2`
