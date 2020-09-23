# How to Create a New Dialect of the Document Files Virtual Schema

This guide helps you to create your custom dialect.

## Check if this is the correct guide

First of all, check if you really need a dialect.
A dialect implements support for an external file provider like BucketFs or S3.
A dialect does not implement support for different file types see the [user_guide](user_guide.md).

Also, check that you really want a new dialect of the files virtual schema. 
If your data source is not a file storage, but for example a database, 
you probably do not want to create a dialect of this Virtual Schema,
but of the [virtual-schema-common-document](https://github.com/exasol/virtual-schema-common-document).

 ## Getting started
 
 To get started with a new dialect create a new repository and copy the contents of the [bucketfs dialect](https://github.com/exasol/bucketfs-document-files-virtual-schema).
 
 ## Adapt the pom.xml file
 
 Now go to the `pom.xml` file and replace all occurrences of `bucketfs` by your file provider.
 If you are not part of the Exasol company, also don't forget to change the organization.
 
 ## `AdapterFactory`
 
 The entry point for each virtual schema is the AdapterFactory. 
 It is loaded via a service loader and build the virtual schema adapter.
 For BucketFS it is the `BucketfsDocumentFilesAdapterFactory`.
 Rename this class to `YOUR_FSDocumentFilesAdapterFactory` and also replace `bucketfs` in `getAdapterVersion()`.
 
 ## The Adapter
 
 Now you will change the actual adapter.
 For that rename the `BucketfsDocumentFilesAdapter`.
 Here you should change the `ADAPTER_NAME`.
 
 The method `getFileLoaderFactory()` gives a `FileLoaderFactory`, which again is a factory for `FileLoaders`.
 The `FileLoader` will be the core of your implementation. 
 It fetches the files from your data source and returns them as `InputStream`.
 
 You can start implementing your `FileLoader` and `FileLoaderFactory` from scratch or inspire yourself from the BucketFS implementation.
 
 There are however some important aspects we want to point out here:
 
 ### GLOB support
 
 Your file loader must support the GLOB syntax (`*` and `?` wildcards).
 If the path definition includes these characters your adapter must search for matching files on your data source.
 
 ### Segmentation
 
 The Virtual Schema adapter distributes the query processing over multiple parallel running workers (UDFs).
Therefore it has to separate the data into multiple segments.
It does this by passing a `SegmentDescription` to your `FileLoaderFactory`. A `SegmentDescription` consists of two numbers.
A total number of segments and a segment id.
Your adapter has to divide the input files into the specified number of segments and only return the contents of the specified segment.
The generic adapter will execute the file loader multiple times in the parallel running workers each time with a different segment id.

Luckily you don't have to implement the segmentation your self. You can use the  `SegmentMatcher` for that purpose.

## Tests

Don't forget to test your dialect.
The BucketFs dialect includes unit and integration tests.
The integration tests use the [exasol-testconatiners](https://github.com/exasol/exasol-testcontainers/), which automatically start a docker version of the Exasol database for testing.

## Support

If you need help, feel free to create a GitHub issue in this repository.

## Tell the World
 
When you finished your dialect, we can list it on the README of this repository, so that others can find it.
Just open an issue!