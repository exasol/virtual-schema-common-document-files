# Virtual Schema for Document Data in Files 9.1.2, released 2026-10-20

Code name: Fix CVE-2026-104880, CVE-2026-90559, CVE-2026-93451, CVE-2026-93452, CVE-2026-108104, CVE-2026-108106, CVE-2026-110262, CVE-2026-110270, CVE-2026-110257, CVE-2026-110258, CVE-2026-110259, CVE-2026-110260, CVE-2026-110261, CVE-2026-110263

## Summary

This release updates dependencies to fix security vulnerabilities in CSV processing, Snappy compression, and ASM bytecode handling. Upgrade to benefit from these security fixes.

## Security

* #222: Fixed vulnerability CVE-2026-104880 in dependency `de.siegmar:fastcsv:jar:3.7.0:compile`
* #218: Fixed vulnerability CVE-2026-90559 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #219: Fixed vulnerability CVE-2026-93451 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #220: Fixed vulnerability CVE-2026-93452 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #224: Fixed vulnerability CVE-2026-108104 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #225: Fixed vulnerability CVE-2026-108106 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #227: Fixed vulnerability CVE-2026-110262 in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #228: Fixed vulnerability CVE-2026-110270 in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #229: Fixed vulnerability CVE-2026-110257 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #230: Fixed vulnerability CVE-2026-110258 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #231: Fixed vulnerability CVE-2026-110259 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #232: Fixed vulnerability CVE-2026-110260 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #233: Fixed vulnerability CVE-2026-110261 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #234: Fixed vulnerability CVE-2026-110263 in dependency `org.ow2.asm:asm:jar:9.10.1:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.1.1` to `2.1.2`
* Updated `de.siegmar:fastcsv:3.7.0` to `4.4.0`

### Test Dependency Updates

* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.22`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
* Updated `org.mockito:mockito-junit-jupiter:5.23.0` to `5.24.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.19` to `2.0.20`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.1` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.5` to `5.8.0`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.codehaus.mojo:build-helper-maven-plugin:3.6.1` to `3.6.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
* Updated `org.sonatype.central:central-publishing-maven-plugin:0.11.0` to `0.12.0`
