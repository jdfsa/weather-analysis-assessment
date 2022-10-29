package com.jdfs.assessment.wheatheranalysis.features.process

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, sum}

case object SegmentsSplitter {

  def groupedData(df: DataFrame): Map[String, DataFrame] =
    Map(
      "monthly" -> monthGrouppedData(df),
      "daily" -> dailyGrouppedData(df),
      "time_interval" -> timeIntervalGrouppedData(df)
    )

  private def monthGrouppedData(df: DataFrame): DataFrame =
    df.groupBy(col("country"), col("month"))
      .agg(sum(col("value")).as("total"))

  private def dailyGrouppedData(df: DataFrame): DataFrame =
    df.groupBy(col("country"), col("date"))
      .agg(sum(col("value")).as("total"))

  private def timeIntervalGrouppedData(df: DataFrame): DataFrame =
    df.groupBy(col("country"), col("interval"))
      .agg(sum(col("value")).as("total"))

}
