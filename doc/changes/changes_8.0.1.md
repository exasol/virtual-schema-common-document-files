# Virtual Schema for Document Data in Files 8.0.1, released 2024-03-11

Code name: Fix CVE-2024-26308, CVE-2024-25710 and CVE-2023-52428 in compile dependencies

## Summary

This release fixes vulnerabilities in the following compile dependencies:
* `org.apache.commons:commons-compress:jar:1.24.0:compile`
  * CVE-2024-26308
  * CVE-2024-25710
* `com.nimbusds:nimbus-jose-jwt:jar:9.8.1:compile`
  * CVE-2023-52428

## Security

* #152: Fixed CVE-2024-26308 in `org.apache.commons:commons-compress:jar:1.24.0:compile`
* #151: Fixed CVE-2024-25710 in `org.apache.commons:commons-compress:jar:1.24.0:compile`
* #150: Fixed CVE-2023-52428 in `com.nimbusds:nimbus-jose-jwt:jar:9.8.1:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.6` to `2.0.7`
* Updated `de.siegmar:fastcsv:2.2.2` to `3.1.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.6.3` to `1.6.5`
* Updated `com.exasol:udf-debugging-java:0.6.11` to `0.6.12`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.4` to `3.15.8`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-junit-jupiter:5.8.0` to `5.11.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.1` to `2.0.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.17` to `4.2.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.11.0` to `3.12.1`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.5`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.5.0` to `1.6.0`
