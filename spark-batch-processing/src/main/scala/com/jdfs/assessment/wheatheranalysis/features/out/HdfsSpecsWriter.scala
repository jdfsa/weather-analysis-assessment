package com.jdfs.assessment.wheatheranalysis.features.out

import org.apache.spark.sql.{DataFrame, SaveMode}

case object HdfsSpecsWriter {

  def writeSpecHdfs(df: DataFrame, path: String): Unit = {
    val fullpath = s"${df.sparkSession.conf.get("weatherapp.hdfs.base_path")}/spec$path"
    df.write.format("parquet")
      .mode(SaveMode.Overwrite)
      .partitionBy("city")
      .save(fullpath)
  }
}
