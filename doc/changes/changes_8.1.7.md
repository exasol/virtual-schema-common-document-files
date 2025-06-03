# Virtual Schema for Document Data in Files 8.1.7, released 2025-??-??

Code name: Fix vulnerabilities CVE-2025-48734 and CVE-2025-4949 in test dependencies

## Summary

This release fixes vulnerabilities CVE-2025-48734 and CVE-2025-4949 in test dependencies.

## Security

* #185: Fixed CVE-2025-48734 in `commons-beanutils:commons-beanutils:jar:1.9.4:test`
* #183: Fixed CVE-2025-4949 in `org.eclipse.jgit:org.eclipse.jgit:jar:6.7.0.202309050840-r:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:11.0.1` to `11.0.3`
* Updated `de.siegmar:fastcsv:3.4.0` to `3.7.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.1`
* Updated `com.exasol:performance-test-recorder-java:0.1.3` to `0.1.4`
* Updated `com.exasol:udf-debugging-java:0.6.14` to `0.6.16`
* Updated `com.exasol:virtual-schema-common-document:11.0.1` to `11.0.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.4` to `5.13.0`
* Updated `org.mockito:mockito-junit-jupiter:5.15.2` to `5.18.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.5.0` to `5.1.0`
* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`
* Added `org.apache.maven.plugins:maven-artifact-plugin:3.6.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.13.0` to `3.14.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.3` to `3.1.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.11.1` to `3.11.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.6.0` to `1.7.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`
