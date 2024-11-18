# Virtual Schema for Document Data in Files 8.1.5, released 2024-11-18

Code name: Fixed vulnerability CVE-2024-47535 in io.netty:netty-common:jar:4.1.104.Final:test

## Summary

This release fixes the following vulnerability:

### CVE-2024-47535 (CWE-400) in dependency `io.netty:netty-common:jar:4.1.104.Final:test`
Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. An unsafe reading of environment file could potentially cause a denial of service in Netty. When loaded on an Windows application, Netty attempts to load a file that does not exist. If an attacker creates such a large file, the Netty application crashes. This vulnerability is fixed in 4.1.115.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-47535?component-type=maven&component-name=io.netty%2Fnetty-common&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-47535
* https://github.com/advisories/GHSA-xq3w-v528-46rv

## Security

* #174: Fixed vulnerability CVE-2024-47535 in dependency `io.netty:netty-common:jar:4.1.104.Final:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.11` to `2.0.12`
* Updated `de.siegmar:fastcsv:3.3.1` to `3.4.0`
* Updated `io.deephaven:deephaven-csv:0.14.0` to `0.15.0`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.1` to `3.17.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.2` to `5.11.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `4.4.0`
* Added `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.2.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-gpg-plugin:3.2.4` to `3.2.7`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.0` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.7.0` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.0` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.12.1` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`
