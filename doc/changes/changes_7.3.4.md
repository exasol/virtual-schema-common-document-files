# Virtual Schema for Document Data in Files 7.3.4, released 2023-09-27

Code name: Fix CVE-2023-42503 and CVE-2023-4759

## Summary

This release fixes the following vulnerabilities in dependencies:
* CVE-2023-42503 in compile dependency `org.apache.commons:commons-compress`
* CVE-2023-4759 in test dependency `org.eclipse.jgit:org.eclipse.jgit`

## Security

* #140: Fixed CVE-2023-42503 and CVE-2023-4759

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:9.4.0` to `9.4.1`
* Updated `io.deephaven:deephaven-csv:0.11.0` to `0.12.0`

### Test Dependency Updates

* Updated `com.exasol:udf-debugging-java:0.6.8` to `0.6.11`
* Updated `com.exasol:virtual-schema-common-document:9.4.0` to `9.4.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`
* Updated `org.mockito:mockito-junit-jupiter:5.4.0` to `5.5.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.3.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.7` to `2.9.12`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.0.1` to `3.1.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`
