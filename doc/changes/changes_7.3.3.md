# Virtual Schema for Document Data in Files 7.3.3, released 2023-07-03

Code name: Upgraded dependencies on top of 7.3.2

## Summary

This release updates dependencies to fix the following vulnerabilities in `org.xerial.snappy:snappy-java`:
* CVE-2023-34453, severity CWE-190: Integer Overflow or Wraparound (7.5)
* CVE-2023-34454, severity CWE-190: Integer Overflow or Wraparound (7.5)
* CVE-2023-34455, severity CWE-770: Allocation of Resources Without Limits or Throttling (7.5)

## Security

* #138: Upgraded dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.3` to `2.0.4`
* Updated `de.siegmar:fastcsv:2.2.1` to `2.2.2`
* Updated `io.deephaven:deephaven-csv:0.10.0` to `0.11.0`
* Updated `jakarta.json:jakarta.json-api:2.1.1` to `2.1.2`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.1` to `3.14.3`
* Updated `org.mockito:mockito-junit-jupiter:5.3.1` to `5.4.0`
