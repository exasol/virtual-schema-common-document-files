# How to Create a new Dialect of the Document Files Virtual Schema

This guide helps you to create your custom dialect.

## Make Sure That you Need a Dialect and not Something Else

First of all, check if you really need a dialect.
A dialect implements support for an external file provider like BucketFs or S3.
A dialect does not implement support for different file types see the [user_guide](user_guide.md).
 
If your data source is not a file storage, but for example a document-database, 
you probably want to create a dialect of the [virtual-schema-common-document](https://github.com/exasol/virtual-schema-common-document).

## Explore Example

As an example consider taking a look into the existing dialects. For example the one for [BucketFS](https://github.com/exasol/bucketfs-document-files-virtual-schema).  
 
## Create the pom.xml File

Start by creating a maven project, define by a `pom.xml` file.
There add this generic base as a dependency:
```xml
        <dependency>
            <groupId>com.exasol</groupId>
            <artifactId>virtual-schema-common-document-files</artifactId>
            <version>VERSION</version>
        </dependency>
``` 

Don't forget to replace `VERSION` with the current latest release number of this repository.

## Create a FileLoader

The best point to start with your implementation is to create a class named `<YOUR_FILE_SOURCE>FileLoader` 
that implements the `FileLoader` interface.
`FileLoader`s fetch the files from your data source and returns them as `InputStream`s.

The file loader interface defines only two methods:

* `loadFiles()`: This method loads the files for a query and returns them as `InputStreamWithResourceName`s (`InputStreamWithResourceName` is a class, that bundles an input stream with a file name used for error messages).
* `getFilePattern()`: Gives a URL or String that describes the path to the files. This description will be included in exceptions. 

You may have noticed that the `loadFiles()` method does not take any arguments.
The reason for that is, that the arguments (which files to load) are passed to the `FileLoaderFactory`.
By that you can implement different `FileLoaders` and a `FileLoaderFactory` that produces them depending on the request.
Typically you will, however, implement a `FileLoaderFactory` that only passes the parameters to the constructor of your `FileLoader`.

Mind the following aspects when implementing the `FileLoader`:

### GLOB support

Your `FileLoader` must support the GLOB syntax (`*` and `?` wildcards).
If the path definition includes these characters your adapter must search for matching files on your data source.

### Segmentation

The Virtual Schema adapter distributes the query processing over multiple parallel-running workers (UDFs).
Therefore it has to separate the data into multiple segments.
It does this by passing a `SegmentDescription` to your `FileLoaderFactory`. A `SegmentDescription` consists of two numbers:
1. a total number of segments
1. a segment id
Your adapter has to divide the input files into the specified number of segments and only return the contents of the specified segment.
The generic adapter will execute the file loader multiple times in the parallel running workers each time with a different segment id.

Luckily you don't have to implement the segmentation your self. You can use the  `SegmentMatcher` for that purpose.


## The Adapter

Now you will create the adapter definition.
Create a new class `<YOUR_FILE_SOURCE>DocumentFilesAdapter` that extends `DocumentFilesAdapter`.

In the file define a constant `ADAPTER_NAME`:

```java
public static final String ADAPTER_NAME = "<YOUR_FILE_SOURCE>_DOCUMENT_FILES";
```

In addition implement the methods `getFileLoaderFactory()` and `getAdapterName()` like:

```json
@Override
protected FileLoaderFactory getFileLoaderFactory() {
    return new BucketFsFileLoaderFactory();
}

@Override
protected String getAdapterName() {
    return ADAPTER_NAME;
}
```

## The AdapterFactory

Finally you need to create a class named `<YOUR_FILE_SOURCE>DocumentFilesAdapterFactory` implementing `AdapterFactory`.
This will be the entry point of your Virtual Schema. 
It is loaded via a service loader and builds the Virtual Schema adapter.

To add it create the file `src/main/resources/META-INF/services/com.exasol.adapter.AdapterFactory` with 
the fully qualified name of your `AdapterFactory` as content:
```
com.example.<YOUR_FILE_SOURCE>DocumentFilesAdapterFactory
```

## Tests

Don't forget to test your dialect.
Take a look at the tests in the BucketFS dialect as an example.
The BucketFs dialect includes unit and integration tests.
The integration tests use the [exasol-testconatiners](https://github.com/exasol/exasol-testcontainers/).

## Support

If you need help, feel free to create a GitHub issue in this repository.

## Tell the World
 
When you finished your dialect, we can list it on the README of this repository, so that others can find it.
Just open an issue!
