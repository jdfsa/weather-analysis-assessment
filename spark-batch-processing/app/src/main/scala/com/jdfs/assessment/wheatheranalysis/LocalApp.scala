package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.CsvReader.readCsv
import com.jdfs.assessment.wheatheranalysis.features.DtParser.detailsDataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, sum}


object LocalApp {

  val spark = SparkSession.builder
    .master("local[*]")
    .appName("WeatherAnalysisBatchProcessing")
    .getOrCreate

  def main(args: Array[String]): Unit = {

    val basePathCurrentDate = "2022/10/26"

    val humidityDf = detailsDataFrame(readCsv(s"/raw/humidity/$basePathCurrentDate/humidity.csv"))
    humidityDf
      .groupBy(col("country"), col("month"))
      .agg(sum(col("value")).as("total"))
      .show(5)

    val pressureDf = detailsDataFrame(readCsv(s"/raw/pressure/$basePathCurrentDate/pressure.csv"))
    val temperatureDf = detailsDataFrame(readCsv(s"/raw/temperature/$basePathCurrentDate/temperature.csv"))
    val weatherDescriptionDf = detailsDataFrame(readCsv(s"/raw/weather_description/$basePathCurrentDate/weather_description.csv"))
    val windDirectionDf = detailsDataFrame(readCsv(s"/raw/wind_direction/$basePathCurrentDate/wind_direction.csv"))
    val windSpeedDf = detailsDataFrame(readCsv(s"/raw/wind_speed/$basePathCurrentDate/wind_speed.csv"))
    val cityAttributesDf = readCsv(s"/raw/city_attributes/$basePathCurrentDate/city_attributes.csv")
      .toDF("City","Country","Latitude","Longitude")

  }
}
