# Virtual Schema for document data in files 3.0.0, released 2021-11-22

Code name: Improved Parquet Loading Performance

## Summary

In this release we improved the performance for loading parquet files. The improvements will give you a speed-up when you load a few (<200) files.

## Features

* #77: Improved performance by distributing small amounts of files explicitly
* #79: Improved performance by splitting reading of big parquet files

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.1.0` to `1.2.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:1.3.1` to `1.3.2`
* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
