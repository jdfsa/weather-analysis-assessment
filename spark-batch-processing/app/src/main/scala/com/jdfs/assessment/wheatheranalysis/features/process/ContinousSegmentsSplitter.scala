package com.jdfs.assessment.wheatheranalysis.features.process

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, RelationalGroupedDataset}

case object ContinousSegmentsSplitter {

  def groupedContinousData(df: DataFrame): Map[String, DataFrame] =
    Map(
      "monthly" -> monthGroupedData(df),
      "daily" -> dailyGroupedData(df),
      "time_interval" -> timeIntervalGroupedData(df)
    )

  private def monthGroupedData(df: DataFrame): DataFrame =
    df.groupBy("country", "month").weatherAggregate()

  private def dailyGroupedData(df: DataFrame): DataFrame =
    df.groupBy("country", "date").weatherAggregate()

  private def timeIntervalGroupedData(df: DataFrame): DataFrame =
    df.groupBy("country", "interval").weatherAggregate()

  private implicit class AggregationsImplicits(df: RelationalGroupedDataset) {
    def weatherAggregate(): DataFrame = df.agg(
      min("value").as("min"),
      max("value").as("max"),
      avg("value").as("average"),
      sum("value").as("total_records")
    )
  }

}
