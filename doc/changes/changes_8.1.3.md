# Virtual Schema for Document Data in Files 8.1.3, released 2024-09-23

Code name: Adapt tests to fixed bug in Exasol

## Summary

This release updates integration tests, adapting to [a fixed bug](https://exasol.my.site.com/s/article/Changelog-content-20991) in `ALTER VIRTUAL SCHEMA`. This allows running the shared integration tests against the latest version of Exasol DB.

## Bugfixes

* #169: Adapted shared integration tests to bugfix in Exasol

## Dependency Updates

### Compile Dependency Updates

* Updated `de.siegmar:fastcsv:3.2.0` to `3.3.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.6.5` to `1.7.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.16.2`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.3` to `5.11.0`
* Updated `org.mockito:mockito-junit-jupiter:5.12.0` to `5.13.0`
