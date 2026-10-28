# Virtual Schema for Document Data in Files 10.0.1, released 2026-10-28

Code name: CSV Delimiter Compatibility

## Summary

This release restores lenient CSV parsing for rows that use a different delimiter after a quoted field. CSV readers with and without headers handle these rows consistently.

## Bug Fixes

* #237: Restored support for CSV rows with a different delimiter after a closing quote.

## Dependency Updates

### Test Dependency Updates

* Removed `org.junit.jupiter:junit-jupiter-params:6.1.3`
* Added `org.junit.jupiter:junit-jupiter:6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.8.0` to `5.8.1`
