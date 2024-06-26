# Virtual Schema for Document Data in Files 8.1.1, released 2024-06-28

Code name: Test `ALTER VIRTUAL SCHEMA <schema> REFRESH`

## Summary

This release adds integration tests that verify that `ALTER VIRTUAL SCHEMA <schema> REFRESH` reads the updated EDML mapping from BucketFS.

**Note:** There is a [known issue](https://exasol.my.site.com/s/article/Changelog-content-20991) in the Exasol database that causes changes to a virtual schema to be lost when only adapter notes are changed but not the schema itself.

**Note:** This adds the following method to `AbstractDocumentFilesAdapterIT`. Update your test to add an implementation for this method.

```java
protected abstract Bucket getBucketFSDefaultBucket();
```

## Dependency Updates

### Compile Dependency Updates

* Updated `de.siegmar:fastcsv:3.1.0` to `3.2.0`

### Test Dependency Updates

* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.10.3`
