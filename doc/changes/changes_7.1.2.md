# Virtual Schema for document data in files 7.1.2, released 2022-10-27

Code name: Fix vulnerabilities 

## Summary

Fixed vulnerabilities
* Updated dependencies:
Updated `com.exasol:performance-test-recorder-java:0.1.1` to `0.1.2` to fix org.apache.commons:commons-text:jar:1.9 in compile
CVE-2022-42889, severity CWE-94: Improper Control of Generation of Code ('Code Injection') (9.8)

* Suppressed following CVE warnings since no updates are available:

    - org.apache.hadoop:hadoop-common:jar:3.3.4: CWE-611: Improper Restriction of XML External Entity Reference ('XXE') (8.6); https://ossindex.sonatype.org/vulnerability/sonatype-2022-5820

    - org.apache.hadoop:hadoop-hdfs-client:jar:3.3.4: CWE-611: Improper Restriction of XML External Entity Reference ('XXE') (8.6); https://ossindex.sonatype.org/vulnerability/sonatype-2022-5732

## Features

* 112: Fix vulnerabilities, updated dependencies, suppressed cve warnings.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.3.3` to `2.0.0`

### Test Dependency Updates

* Updated `com.exasol:performance-test-recorder-java:0.1.1` to `0.1.2`
* Updated `org.mockito:mockito-core:4.8.0` to `4.8.1`
* Updated `org.mockito:mockito-junit-jupiter:4.8.0` to `4.8.1`
