# Virtual Schema for Document Data in Files 8.1.4, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-47561 in org.apache.avro:avro:jar:1.11.3:compile

## Summary

This release fixes the following vulnerability:

### CVE-2024-47561 (CWE-502) in dependency `org.apache.avro:avro:jar:1.11.3:compile`
Schema parsing in the Java SDK of Apache Avro 1.11.3 and previous versions allows bad actors to execute arbitrary code.
Users are recommended to upgrade to version 1.11.4Â  or 1.12.0, which fix this issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-47561?component-type=maven&component-name=org.apache.avro%2Favro&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-47561
* https://lists.apache.org/thread/c2v7mhqnmq0jmbwxqq3r5jbj1xg43h5x

## Security

* #171: Fixed vulnerability CVE-2024-47561 in dependency `org.apache.avro:avro:jar:1.11.3:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `de.siegmar:fastcsv:3.3.0` to `3.3.1`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.2` to `3.17.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.0` to `5.11.2`
* Updated `org.mockito:mockito-junit-jupiter:5.13.0` to `5.14.1`
