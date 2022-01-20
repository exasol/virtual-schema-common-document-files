# Virtual Schema for document data in files 5.0.0, released 2022-01-20

Code name: Unified connection definition

This release added common implementation parts for switching to unified connection definition specified in: https://github.com/exasol/connection-parameter-specification/.

This release has breaking API changes:

* The dialects must now implement UdfEntryPoint and call GenericUdfCallHandler.
* The dialect Java API changed

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.0` to `0.4.1`
* Updated `com.exasol:parquet-io-java:1.2.0` to `1.3.0`
* Updated `com.exasol:virtual-schema-common-document:7.0.0` to `8.0.0`

### Test Dependency Updates

* Updated `com.exasol:udf-debugging-java:0.4.1` to `0.5.0`
* Updated `com.exasol:virtual-schema-common-document:7.0.0` to `8.0.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.1` to `5.8.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.1` to `5.8.2`
* Updated `org.mockito:mockito-core:4.0.0` to `4.2.0`
* Updated `org.mockito:mockito-junit-jupiter:4.0.0` to `4.2.0`
