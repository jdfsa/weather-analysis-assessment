package com.jdfs.assessment.wheatheranalysis.features.in

import org.apache.spark.sql.{DataFrame, SparkSession}

class CsvReader(sparkSession: SparkSession) {

  def readRawCsv(path: String): DataFrame = {
    val fullpath = s"${sparkSession.conf.get("weatherapp.hdfs.base_path")}/raw$path"
    print(s"CSV INPUT: ${fullpath}")
    sparkSession.read.option("header", "false").option("charset", "UTF-8")
      .csv(fullpath)
  }

}
