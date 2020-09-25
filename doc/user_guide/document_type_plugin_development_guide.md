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

So start with creating a new class called `YOUR_TYPEDocumentFetcher` that implements `DocumentFetcher<YOUR_TYPEDocumentNodeVisitor>`.

The interface requires only a single method:

```java
public Stream<DocumentNode<YOUR_TYPEDocumentNodeVisitor>> run(final ExaConnectionInformation connectionInformation) {
```

Inside of this method you need to load the data, and convert it into your class structure implementing the `DocumentNode` interfaces.

For the loading of the files, use a `FileLoaderFactory` that you add as constructor parameter.
By that dependency injection you can use your document type implementation with different file backends (dialects).

In the run method you can use:

```java
final Stream<InputStream> jsonStream = this.fileLoaderFactory
                .getLoader(this.filePattern, this.segmentDescription, connectionInformation).loadFiles();
```

Then convert the Stream<InputStream> into a Stream<DocumentNode<YOUR_TYPEDocumentNodeVisitor>> by parsing it and using the factory for your class structure.

Don't forget to close the input streams!

## The DataLoader

Finally we implement the a `DataLoader`.





