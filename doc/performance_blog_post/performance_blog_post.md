# How we Improved the Files Virtual Schema Performance

Our [Virtual Schemas for document files](https://github.com/exasol/virtual-schema-common-document-files) allow our customer to integrate data from files into their Exasol database. Customers use this feature for integrating external data on demand into their analytics and for building ETL pipelines.

For Exasol speed matters. So does it for our integrations. For customers its crucial that the queries that include external data run as fast as possible. In this blog post I want to give some insight on the process of our recent performance improvements of the Virtual Schema for parquet files.

## How it Begins

For me this project started when our product manager asked me to improve the performance of the parquet file loading. He compared the performance of our products with competitors and found out, that parquet loading needs some improvements.

In order to do professional performance improvements we use the simple workflow:

Measure -> Improve -> Measure.

In my eyes, this simple strategy brings lots ot advantages:  
First it allows you to check weather a change helped or if it even made some functionality slower. For example improving the performance of loading big parquet files can easily slow down the loading of many small files. Second, this strategy allows you to define a goal when to stop. Otherwise, you will keep improving forever since there's always some space for improvements.

## Writing Performance Tests

So my first step was to write automated performance tests for the parquet loading. Don't get this wrong. It's not that we did not have any tests before. Of course, we had unit and integration tests for the Virtual Schema that make sure that the Virtual Schema works as expected. But since this is a rather new product we did not have automated performance tests yet.

Until now, we checked the performance of this Virtual Schema manually by starting a cluster and querying a parquet dataset. However, for these performance improvements we needed more test cases and also more reliability.

While for our integration tests we use the docker version of Exasol (which is absolutely sufficient for functional testing), for the performance test we want to test with a real-live setup. So we decided to test with a 4 node cluster (with c5.4xlarge instances) on AWS. There we load the files from S3 using the [s3-document-files-virtual-schema](https://github.com/exasol/s3-document-files-virtual-schema).

The virtual Schemas are built out of multiple layers:

![Stack of virtual schema layers](vsDocStack.png)

The parquet loading implementation is located in the Document Files Virtual Schema. However, we can't test this directly. Instead, we decided to test the S3 implementation, since this is the most popular dialect.

### Test Quality

We measure the query performance by measuring the execution time of a query on a Virtual Schema table. By that we measure the full execution time including network transmission, S3 loading and so on. This of course adds some noise to our measurement. However, we decided for this strategy since it measures the time that our customers can expect.

In order to make the tests more reliable we run each test 5 times. Then we drop the first 2 results, since they are usually slower, since S3 will keep the files in cache for the following runs. Then we build the average of the remaining 3 runs. We decided for the average since it will again result in a value that customers can expect.

### Test Fixtures

For our performance test we need some test fixtures. For example one big parquet file on S3 that we load with a Virtual Schema and measure the performance.

The simplest option for this is to create this file with some tool locally and store it forever on S3. This approach has the advantage, that its easy to create. On the other hand, it's hard to change, and it's also not apparent from the source code how the test data is created. Since out projects are open source, its also important that all (non Exasol-) contributors can run the tests. So we would need to make our S3 bucket publicly available which could cause costs.

These issues can be solved by creating the test data on-the-fly before running the test. Then the test-data is documented and under version control. That can be very helpful, to make sure that there were no changes on the test fixture between two runs.

One downside of this approach is that it only works for rather small test files. For larger files, creating and uploading the files gets too expensive and slow. To solve this we added a cache: Before uploading the test files, we crate a checksum and check if the files are already in place. Only if something was changed we re-upload the files.

## Displaying Performance Tests Results

In order to evaluate the test results, and discuss them, we need to display them somehow. For that we decided for a [Redash](https://redash.io/) dashboard. Redash is an open source tool that allows you to create online dashboards for SQL queries. So we push our performance test results into an Exasol database and use Redash to evaluate them.

## Setting Goals

The first test run generated the following results:

| Test Case                          | Time in s |
|------------------------------------|-----------|
| testLoadManyParquetRows            | 23        |
| testLoadLargeParquetRows           | 13.02     |
| testLoadManyParquetColumns         | 12.9      |
| testLoadManyParquetRowsFromOneFile | 74.64     |
| testManySmallJsonFiles             | 685.87    |
| testLoadFewBigSalesParquetFiles    | 662.28    |
| testLoadSalesParquetFiles          | 219.21    |

The first 4 cases (`testLoadManyParquetRows`, `testLoadLargeParquetRows`, `testLoadManyParquetColumns` `testLoadManyParquetRowsFromOneFile`) all load the same amount of data. They just differ on how the data is distributed over rows, files and columns.

The same applies for the two trailing tests: While `testLoadFewBigSalesParquetFiles` distributes the rows over 200 files, `testLoadSalesParquetFiles` only over 8 files.

![Performance test results of parquet file loading 1](slowParquetTestResults1.png)
![Performance test results of parquet file loading 2](slowParquetTestResults2.png)

In the charts it becomes clear that the Virtual Schema at that point has an issue with loading data from few big files. When loading from more files, it loads the same amount of data a lot quicker.

Starting with this analysis, we discussed with our product management and decided to invest into performance improvements for loading big parquet files. More precisely we set the goal to make improvements so that the `testLoadFewBigSalesParquetFiles` also runs in less than 250s.

## Improving the Performance

Next we analyzed the source code for possible reasons for the slow reading of big parquet files. There we identified the following issues:

* Unequal distribution
* Too few parallelization for few parquet files

The query processing of the Files Virtual Schemas consists of two phases: A planning phase and an execution phase. In the planning phase the adapter analyzes the query and starts multiple workers (UDFs) that then run the execution phase. For technical reasons the workers can not communicate at the moment. So the Virtual Schema parallelizes the import job over multiple workers.

### Distributing the File

Until now this parallelization was realized using hash binning. So each worker received a range and then loaded files that names hash values fell into that range. That approach is great for distributing lots of files, since it does not require to list all files in advance. Doing so could cause memory overflow when distributing for example 1,000,000 files with long names.

However, for only a few files, this works rather bad:
Imagine distributing 4 files over 4 workers. A good distribution is obvious here. Each worker receives on file. The workers then load the files in parallel. With hash binning it's however quite likely, that one worker receives two files and another none. This will increase the time for loading.

![Example of file distribution](fileDistribution.png)

To fix this issue we changed the implementation to distribute smaller amounts of files explicitly and only use hash binning for larger amounts.

### Splitting Large Files

The second problem occurs, if there are 4 workers but only one file. In that case only one worker will be used. To mitigate this we decided to split the file into multiple parts. That's not possible for all file formats. For example with JSON files where each file contains one document / row. For parquet, however it is. Parquet files contain multiple rows and group these rows into blocks (row groups). We changed our reader so that it's capable of only reading specific row groups. By that, we can again distribute the work over all available workers.

## Results

![Chart showing the performance improvements](results.png)

The chart shows the duration of the test runs. Run nr 1 is the one we discussed earlier. Run 2 was measured after we improved the distribution. Run 3 after implementing the file splitting.

The result prove that our improvements had the desired effect. Distributing the files explicitly helped a bit for `testLoadFewBigSalesParquetFiles`. We expected this since before we had the risk that the 8 files wore not distributed equally over the workers.

The second improvement with splitting the files helped a lot for `testLoadFewBigSalesParquetFiles` and `testLoadManyParquetRowsFromOneFile`.