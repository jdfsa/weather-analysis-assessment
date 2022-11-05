# Weather Analysis | Capstone Project

This repository contains all the artifacts generated uniquely for the Capstone Project / Assessment done as part of the PGP in Data Engineering - Weather Analysis.

The objective of this project is to put into practice Data Engineering concepts by building a data pipeline that will take csv files, clean and tranfrom the data, and make it available in DocumentDB and S3, so that can be acessed by other tools.

## Architecture

The following architecture was proposed to accomplish the assessment:

![architecture diagram](architecture.jpg?raw=true)

## Repository contents

You will find in the repo the following contents:

* **dataset**: all csv data files used in the process; the data can also be downloaded from the official website: [kaggle.com](https://www.kaggle.com/selfishgene/historical-hourly-weather-data)
* **kafka-producer**: is the application used to send csv data "as is" to kafka topic
* **kafka-stream-processor**: application responsible for reading the source topic, cleaning data by removing records having null/empty values, and sending to a specific kafka topic according to its origin
* **flume_setup**: containg all files to setup flume agent locally; the agent will grab all stream data from kafka topics and write to HDFS as CSV raw format
* **spark-batch-processing**: application responsible for reading CSV raw format files stored in HDFS, generating a specialized view by transforming them as required in the project, and write to three destinations:
    * HDFS
    * S3
    * DocumentDB
* **emr_setup.sh**: contains the shell scripts that will prepare the EMR cluster with all required files and background services; it will also install complementary linux apps for this purpose
* **producing-data.sh**: contains the scripts that will help start configuring the EMR cluster (by running `emr_setup.sh` script) and start generating/processing the data
* **upload-files.sh**: it's a helper script that will upload all contents to S3, following the correct folder structure that's required by other components

## Compile

Use the following commands to generate jar files for the three applications: kafka-producer, kafka-stream-processor, spark-batch-processing

```shell
# build kafka producer app
mvn clean install -Dmaven.test.skip=true -f ./kafka-producer/pom.xml

# build kafka stream processor app
mvn clean install -Dmaven.test.skip=true -f ./kafka-stream-processor/pom.xml

# build spark batch processor app
mvn clean install -Dmaven.test.skip=true -f ./spark-batch-processing/pom.xml
```
