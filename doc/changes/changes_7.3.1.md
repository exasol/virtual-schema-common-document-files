# Virtual Schema for Document Data in Files 7.3.1, released 2023-05-03

Code name: CSV Performance Regression Tests

## Summary

This release updates performance regression rests for CSV files to use all data types (string, boolean, integer, double, date and timestamp) instead of only string. Please note that this might influence comparability of test results. Additionally the test names in the test report changed. They now use suffix `()` instead of `(TestInfo)`.

## Features

* #134: Added CSV data type performance regression tests

## Dependency Updates

### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
