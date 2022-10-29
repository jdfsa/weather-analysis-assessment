package com.jdfs.assessment.wheatheranalysis.features.out

import com.jdfs.assessment.wheatheranalysis.features.FeaturesConstants.BASE_CSV_RAW_PATH
import org.apache.spark.sql.{DataFrame, SaveMode}

case object HdfsSpecsWriter {

  def writeSpec(df: DataFrame, path: String): Unit = {
    df.write.format("csv")
      .mode(SaveMode.Overwrite)
      .partitionBy("country")
      .save(s"$BASE_CSV_RAW_PATH/spec$path")
  }
}
