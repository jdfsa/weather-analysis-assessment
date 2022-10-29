package com.jdfs.assessment.wheatheranalysis.features

import com.jdfs.assessment.wheatheranalysis.LocalApp.spark
import org.apache.spark.sql.DataFrame

case object CsvReader {

  val BASE_CSV_RAW_PATH = "file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset-hadoop"

  def readCsv(path: String): DataFrame =
    spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/$path")

}
