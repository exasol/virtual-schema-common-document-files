# User Guide

This user guide helps you with getting started with the Files Virtual Schemas.

### Installation
 
Upload the latest available [release of this adapter](https://github.com/exasol/virtual-schema-common-document-files/releases) to BucketFS.
See [Create a bucket in BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/create_new_bucket_in_bucketfs_service.htm) and [Upload the driver to BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/accessfiles.htm) for details.

Then create a schema to hold the adapter script.

```
CREATE SCHEMA ADAPTER;
```

Next create the Adapter Script:
 ```
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.FILES_ADAPTER AS
    %scriptclass com.exasol.adapter.RequestDispatcher;
    %jar /buckets/bfsdefault/default/document-virtual-schema-dist-2.0.0-SNAPSHOT-files-0.2.0.jar;
/
```

In addition to the adapter script you need to create a UDF function that will handle the loading of the data:
```
CREATE OR REPLACE JAVA SET SCRIPT ADAPTER.IMPORT_FROM_DOCUMENT(
  DATA_LOADER VARCHAR(2000000),
  REMOTE_TABLE_QUERY VARCHAR(2000000),
  CONNECTION_NAME VARCHAR(500))
  EMITS(...) AS
    %scriptclass com.exasol.adapter.document.UdfEntryPoint;
    %jar /buckets/bfsdefault/default/document-virtual-schema-dist-2.0.0-SNAPSHOT-files-0.2.0.jar;
/
```

## Creating a Virtual Schema
 
Now you need to define a connection to place where the Files are stored:

* BucketFS:
     ```
    CREATE CONNECTION BUCKETFS_CONNECTION
        TO 'bucketfs:/bfsdefualt/default/'
        USER ''
        IDENTIFIED BY '';
    ```
  You can leave `USER` and `IDENTIFIED BY` empty.
  
  The path after `buketfs:` is the base path for the file names you define in the mapping definition.
  The adapter will concatenate the base path from the connection and the path defined in the mapping definition. 
  For security reasons you can however not navigate to directories outside of the base path (using `../`). 

Before creating a Virtual Schema you need to [create mapping definitions](create_a_mapping_definition.md) and upload them to a BucketFS bucket.

Finally create the Virtual Schema using:

```
CREATE VIRTUAL SCHEMA FILES_VS_TEST USING ADAPTER.FILES_ADAPTER WITH
    CONNECTION_NAME = 'BUCKETFS_CONNECTION'
    SQL_DIALECT     = 'DOCUMENT_FILES'
    MAPPING         = '/bfsdefault/default/path/to/mappings/in/bucketfs';
```

The CREATE VIRTUAL SCHEMA command accepts the following properties:

| Property          | Mandatory   |  Default      |   Description                                                                   |
|-------------------|-------------|---------------|---------------------------------------------------------------------------------|
|`MAPPING`          | Yes         |               | Path to the mapping definition file(s)                                          |
|`MAX_PARALLEL_UDFS`| No          | -1            | Maximum number of UDFs that are executed in parallel. -1 represents unlimited. *| 
 
 \* The adapter will start at most one UDF per input file. 
 That means, if data from a single file (for example a JSON-Lines file) is loaded, it will not parallelize.
 
Now browse the data using your favorite SQL client.
