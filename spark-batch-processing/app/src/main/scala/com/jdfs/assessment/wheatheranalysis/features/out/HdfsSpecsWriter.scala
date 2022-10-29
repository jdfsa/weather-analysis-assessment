package com.jdfs.assessment.wheatheranalysis.features.out

import com.jdfs.assessment.wheatheranalysis.features.FeaturesConstants.BASE_CSV_RAW_PATH
import org.apache.spark.sql.{DataFrame, SaveMode}

case object HdfsSpecsWriter {

  def writeSpecHdfs(df: DataFrame, path: String): Unit = {
    df.write.format("parquet")
      .mode(SaveMode.Overwrite)
      .partitionBy("city")
      .save(s"$BASE_CSV_RAW_PATH/spec$path")
  }
}
