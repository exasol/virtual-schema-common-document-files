# How to add Support for a new Document Type

This guide explains how to add a support for a new document types like JSON or JSON-Lines.

If you want to add support for a different files backend like BucketFS or S3, use the [dialect development guide](dialect_development_guide.md).

## The Document Node Interface
This Document Virtual Schemas abstract over the implementation of the document data.
By that it can support different document types.
For that we use the `DocumentNode` interface structure:

![document node](documentnode.png)

The idea behind this structure is, that this Virtual Schema adapter can travers your document data, without knowing 
implementation details, like the language specific data types.

Implement this structure in the following steps:

* Select a name for your document structure (in this example we will use `YOUR_TYPE`)
* Define a Visitor interface for your type (`YOUR_TYPEDocumentNodeVisitor`)
* Implement wrappers for the java API classes of your data type that implement `DocumentArray`, `DocumentObject` or `DocumentValue`.
  You can define multiple classes for each interface. For example `YOUR_TYPENumber` and `YOUR_TYPEString` that both implement `DocumentValue`.
  As a type parameter for the generic interfaces use your Visitor.
* Implement a factory that wraps the Java classes of your data type into the newly defined wrapper classes.

If your data type does not has a Java API you can also create your own parser, that implements these interfaces. 
  
 ## The Property to Column Value Extractors
 
 Now we need to implement the value extraction from the class structure that wwe just defined.
 
 At the moment there are three types of mappings:
 
 * `toVarcharMapping`
 * `toDecimalMapping`
 * `toJsonMappings`
 
 To learn more about the mappings check the [EDML documentation](https://github.com/exasol/virtual-schema-common-document/blob/master/doc/user_guide/edml_user_guide.md).  

We now implement so called PropertyToColumnValueExtractors that extract the desired value from the class structure.

Therefore we create a class `YOUR_TYPEPropertyToVarcharColumnValueExtractor` that extends `PropertyToVarcharColumnValueExtractor<YOUR_TYPEDocumentNodeVisitor>` and implement the required methods.

Hint: You can use the your a visitor for your DocumentNode structure defined to dispatch between the different types here.

Implement the value extractors for the other mappings respectively.

After implementing extractors for all mapping types, you need to create a factory for them.

Therefore we create `YOUR_TYPEPropertyToColumnValueExtractorFactory` that implements `PropertyToColumnValueExtractorFactory<YOUR_TYPEDocumentNodeVisitor>`.
Don't forget to use your Visitor as generic type here.

## The DocumentFetcher

Now we implement the `DocumentFetcher` interface.
The `DocumentFetcher` has the task to fetch the document data for a given query from the data source.
For Virtual Schemas that load files, we can use the abstract base implementation `AbstractFilesDocumentFetcher`.  

So start with creating a new class called `YOUR_TYPEDocumentFetcher` that extends `AbstractFilesDocumentFetcher<YOUR_TYPEDocumentNodeVisitor>`.

The interface requires only a single method:

```java
protected abstract Stream<DocumentNode<DocumentVisitorType>> readDocuments(InputStreamWithResourceName loadedFile);
```

Inside of this method you need to load the data, and convert it into your class structure implementing the `DocumentNode` interfaces.
Tha parameter `InputStreamWithResourceName` consists of an `InputStream` from which you can parse the data and a file name, that you can use for logging. 

Implement the method so that it converts the `loadedFile` into a `Stream<DocumentNode<YOUR_TYPEDocumentNodeVisitor>>` by parsing it and using the factory for your class structure.
If your datatype has only one document per file, simply return `Set.of(YOUR_DOCUMTNT_NODE)`.
Else it is important to stream the data and not to collect it in a List first. 
For that implement an custom `Iterator` and `Iterable` and use:

```java
return StreamSupport.stream(new YOUR_ITERABLE(loadedFile).spliterator(), false);
```

Don't forget to close the input streams!

## The DataLoader

Next, we implement the a `DataLoader`. The data loader runs the whole pipeline from fetching the data, 
extracting the selecting properties over mapping the result to Exasol values.
Luckily most of this implementation is covered in it's abstract basis `AbstractDataLoader`.

Create a new class `YOUR_TYPEFilesDataLoader` that extends `AbstractDataLoader<YOUR_TYPEDocumentNodeVisitor>`.

Implement the constructor like:

```java
public YOUR_TYPEFilesDataLoader(final DocumentFetcher<YOUR_TYPEDocumentNodeVisitor> documentFetcher) {
        super(documentFetcher);
}
```

We will call it later from the `YOUR_TYPEDataLoaderFactory`.

Next implement `getValueExtractorFactory` like:

```java
@Override
protected PropertyToColumnValueExtractorFactory<YOUR_TYPEDocumentNodeVisitor> getValueExtractorFactory() {
    return new YOUR_TYPEPropertyToColumnValueExtractorFactory();
}
```
Note that we return here the `PropertyToColumnValueExtractorFactory` that we defined earlier.

## The FilesDataLoaderFactory

Finally we add a factory for the newly defined `DataLoader`.
For that create a new class named `YOUR_TYPEFilesDataLoaderFactory`:

```java
public class YOUR_TYPEFilesDataLoaderFactory implements FilesDataLoaderFactory {
    @Override
    public List<DataLoader> buildDataLoaderForQuery(final RemoteTableQuery remoteTableQuery,
            final int maxNumberOfParallelFetchers, final FileLoaderFactory fileLoaderFactory) {
        final List<DataLoader> dataLoaders = new ArrayList<>(maxNumberOfParallelFetchers);
        for (int segmentCounter = 0; segmentCounter < maxNumberOfParallelFetchers; segmentCounter++) {
            final JsonDocumentFetcher documentFetcher = new YOUR_TYPEDocumentFetcher(
                    remoteTableQuery.getFromTable().getRemoteName(),
                    new SegmentDescription(maxNumberOfParallelFetchers, segmentCounter), fileLoaderFactory);
            dataLoaders.add(new YOUR_TYPEFilesDataLoader(documentFetcher));
        }
        return dataLoaders;
    }

    @Override
    public List<String> getSupportedFileExtensions() {
        return List.of(".YOUR_TYPE");
    }
}
```



