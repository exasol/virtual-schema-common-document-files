# Creating a Mapping Definition for Document Files

This guide shows you how to define a mapping definition for a [JSON-Lines](https://jsonlines.org/)  file.

A very simple mapping definition for a [JSON-Lines](https://jsonlines.org/) file looks like that:

```json
{
  "$schema": "https://schemas.exasol.com/edml-1.0.0.json",
  "source": "test.jsonl",
  "destinationTable": "BOOKS",
  "mapping": {
    "fields": {
      "id": {
        "toVarcharMapping": {
        }
      }
    }
  }
}
```

The `source` property defines the path to the file relative to the base path defined in the `CONNECTION`.

The virtual schema adapter automatically detects the type of the file from the file extension:

| Ending   | Type                                           |
|----------|------------------------------------------------|
|`.json`   | JSON file                                      |
|`.jsonl`  | [JSON-Lines](https://jsonlines.org/)  file     |

For description and examples for defining the mapping of the document data see the [DynamoDB VS documentation](https://github.com/exasol/dynamodb-virtual-schema/blob/master/doc/gettingStartedWithSchemaMappingLanguage.md).
