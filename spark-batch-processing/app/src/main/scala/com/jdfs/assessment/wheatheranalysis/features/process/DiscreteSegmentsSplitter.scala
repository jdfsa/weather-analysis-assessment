package com.jdfs.assessment.wheatheranalysis.features.process

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object DiscreteSegmentsSplitter {

  def groupedDiscreteData(df: DataFrame): Map[String, DataFrame] =
    Map(
      "monthly" -> monthGroupedData(df),
      "daily" -> dailyGroupedData(df),
      "time_interval" -> timeIntervalGroupedData(df)
    )

  private def monthGroupedData(df: DataFrame): DataFrame = {
    val dfTotal = df
      .groupBy("country", "month")
      .agg(count("month").as("total_month"))

    val dfDescription = df
      .groupBy("country", "month", "value")
      .agg(count("value").alias("total_description"))

    dfDescription
      .join(dfTotal, dfDescription("country").equalTo(dfTotal("country")) &&
        dfDescription("month").equalTo(dfTotal("month")), "inner")
      .withColumn("percent", round(col("total_description")
        .divide(col("total_month")).multiply(lit(100)), 4))
      .select(
        dfDescription("country"),
        dfDescription("month"),
        col("value").as("description"),
        col("total_description").as("total"),
        col("total_month").as("total_monthly"),
        col("percent")
      )
  }

  private def dailyGroupedData(df: DataFrame): DataFrame = {
    val dfTotal = df
    .groupBy("country", "date")
    .agg(count("month").as("total_date"))

    val dfDescription = df
    .groupBy("country", "date", "value")
    .agg(count("value").alias("total_description"))

    dfDescription
      .join(dfTotal, dfDescription("country").equalTo(dfTotal("country")) &&
        dfDescription("date").equalTo(dfTotal("date")), "inner")
      .withColumn("percent", round(col("total_description")
        .divide(col("total_date")).multiply(lit(100)), 4))
      .select(
        dfDescription("country"),
        dfDescription("date"),
        col("value").as("description"),
        col("total_description").as("total"),
        col("total_date").as("total_daily"),
        col("percent")
      )
  }

  private def timeIntervalGroupedData(df: DataFrame): DataFrame = {
    val dfTotal = df
      .groupBy("country", "interval")
      .agg(count("interval").as("total_interval"))

    val dfDescription = df
      .groupBy("country", "interval", "value")
      .agg(count("value").alias("total_description"))

    dfDescription
      .join(dfTotal, dfDescription("country").equalTo(dfTotal("country")) &&
        dfDescription("interval").equalTo(dfTotal("interval")), "inner")
      .withColumn("percent", round(col("total_description")
        .divide(col("total_interval")).multiply(lit(100)), 4))
      .select(
        dfDescription("country"),
        dfDescription("interval"),
        col("value").as("description"),
        col("total_description").as("total"),
        col("total_interval"),
        col("percent")
      )
  }

}
