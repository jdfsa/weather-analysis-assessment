package com.jdfs.assessment.wheatheranalysis.features.out

import org.apache.spark.sql.{DataFrame, SaveMode}

case object S3SpecsWriter {

  def writeSpecS3(df: DataFrame, path: String): Unit = {
    val fullpath = s"${df.sparkSession.conf.get("weatherapp.s3.base_path")}/spec$path"
    df.write.format("parquet")
      .mode(SaveMode.Overwrite)
      .partitionBy("city")
      .save(fullpath)
  }
}
