package com.jdfs.assessment.wheatheranalysis.features.in

import com.jdfs.assessment.wheatheranalysis.LocalApp.spark
import org.apache.spark.sql.DataFrame

case object CsvReader {

  def readRawCsv(path: String): DataFrame = {
    val fullpath = s"${spark.conf.get("weatherapp.hdfs.base_path")}/raw$path"
    spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(fullpath)
  }

}
