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

* Select a name for your document structure (in this example we will use Json)
* Define a Visitor interface for your type (`JsonDocumentNodeVisitor`)
* Implement wrappers for the java API classes of your data type that implement `DocumentArray`, `DocumentObject` or `DocumentValue`.
  You can define multiple classes for each interface. For example `JsonNumber` and `JsonString` that both implement `DocumentValue`.
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

Therefore we create a class `JsonPropertyToVarcharColumnValueExtractor` that extends `PropertyToVarcharColumnValueExtractor<JsonNodeVisitor>` and implement the required methods.

Hint: You can use the your a visitor for your DocumentNode structure defined to dispatch between the different types here. 


