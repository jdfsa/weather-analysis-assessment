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

    spark.sparkContext.parallelize(Seq("humidity", "pressure", "temperature", "wind_direction", "wind_speed"))
      .foreach(src => {
        readRawCsv(s"/$src/$basePathCurrentDate/$src.csv")
          .weatherCitiesFormat().weatherSplitGroups()
          .foreach(item => item._2.weatherSaveSpecData(s"/$src/$basePathCurrentDate/${item._1}"))
      })

    val weatherDescriptionDf = readRawCsv(s"/weather_description/$basePathCurrentDate/weather_description.csv")
      .weatherCitiesFormat().weatherSplitGroups()

    val cityAttributesDf = readRawCsv(s"/city_attributes/$basePathCurrentDate/city_attributes.csv")
      .toDF("City","Country","Latitude","Longitude")

  }
}
