# Virtual Schema for Document Data in Files 8.0.2, released 2024-04-12

Code name: Fix reading duplicate values

## Summary

This release fixes a bug introduced with version [8.0.1](https://github.com/exasol/virtual-schema-common-document-files/releases/tag/8.0.1) / PR [#153](https://github.com/exasol/virtual-schema-common-document-files/pull/153). When reading a CSV with headers and duplicate values like the following

```csv
header1,header2
duplicate-value,duplicate-value
value1,value2
```

the virtual schema query failed with error

````
de.siegmar.fastcsv.reader.CsvParseException: Exception when reading record that started in line 2
...
Caused by: java.lang.IllegalStateException: E-VSDF-72: Duplicate field 'duplicate-value' at line number 2 / field index 1, all fields: ['header2', 'duplicate-value', 'header1']
...
```

## Bugfix

* #154: Fixed bug when reading CSV with duplicate values

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:10.1.0` to `10.1.1`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document:10.1.0` to `10.1.1`
