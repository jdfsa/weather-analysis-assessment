package com.jdfs.assessment.wheatheranalysis.features.in

import com.jdfs.assessment.wheatheranalysis.LocalApp.spark
import com.jdfs.assessment.wheatheranalysis.features.FeaturesConstants.BASE_CSV_RAW_PATH
import org.apache.spark.sql.DataFrame

case object CsvReader {

  def readRawCsv(path: String): DataFrame =
    spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw$path")

}
