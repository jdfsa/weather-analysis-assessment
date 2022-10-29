package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.FeaturesImplicits.Functions
import com.jdfs.assessment.wheatheranalysis.features.in.CsvReader.readRawCsv
import org.apache.spark.sql.SparkSession

object LocalApp {

  val spark = SparkSession.builder
    .master("local[*]")
    .appName("WeatherAnalysisBatchProcessing")
    .getOrCreate

  def main(args: Array[String]): Unit = {

    val basePathCurrentDate = "2022/10/26"

    // city attributes
    readRawCsv(s"/city_attributes/$basePathCurrentDate/city_attributes.csv")
      .toDF("City","Country","Latitude","Longitude")
      .weatherWriteSpecData(s"/city_attributes/$basePathCurrentDate/")

    // weather description
    readRawCsv(s"/weather_description/$basePathCurrentDate/weather_description.csv")
      .weatherToCitiesFormat()
      .weatherToDiscreteGroups()
      .foreach(item => item._2.weatherWriteSpecData(s"/weather_description/$basePathCurrentDate/${item._1}"))

    // other data
    spark.sparkContext.parallelize(Seq(
      "humidity", "pressure", "temperature", "wind_direction", "wind_speed")
    ).foreach(src => {
        readRawCsv(s"/$src/$basePathCurrentDate/$src.csv")
          .weatherToCitiesFormat().weatherToContinousGroups()
          .foreach(item => item._2.weatherWriteSpecData(s"/$src/$basePathCurrentDate/${item._1}"))
      })
  }
}
