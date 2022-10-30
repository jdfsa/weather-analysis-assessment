package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.FeaturesImplicits.{ArrayFunctions, DataFrameFunctions}
import com.jdfs.assessment.wheatheranalysis.features.in.CsvReader
import org.apache.spark.sql.SparkSession

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalApp {

  def main(args: Array[String]): Unit = {
    val argsMap = args.weatherToMapArgs()
    val spark = SparkSession.builder
      .appName("WeatherAnalysisBatchProcessing")
      .master(argsMap.get("spark_master").getOrElse("local[*]"))
      .config("weatherapp.hdfs.base_path", argsMap.get("weatherapp.hdfs.base_path")
        .getOrElse("file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset-hadoop"))
      .config("weatherapp.mongodb.base_connection", argsMap.get("weatherapp.mongodb.base_connection")
        .getOrElse("mongodb://127.0.0.1:27017/weather"))
      .getOrCreate
    val csvReader = new CsvReader(spark)

    val basePathCurrentDate = argsMap.get("weatherapp.reference_date")
      .getOrElse(LocalDate.now.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))))
      .replace("-", "/")

    // city attributes
    csvReader.readRawCsv(s"/city_attributes/$basePathCurrentDate/city_attributes.csv")
      .toDF("City","Country","Latitude","Longitude")
      .weatherWriteToSpecHdfs(s"/city_attributes/$basePathCurrentDate/")
      .writeToSpecMongoDb("city_attributes")

    // weather description
    csvReader.readRawCsv(s"/weather_description/$basePathCurrentDate/weather_description.csv")
      .weatherToCitiesFormat()
      .weatherToDiscreteGroups()
      .foreach(item => item._2
        .weatherWriteToSpecHdfs(s"/weather_description/$basePathCurrentDate/${item._1}")
        .writeToSpecMongoDb(s"weather_description_${item._1}"))

    // other data
    spark.sparkContext.parallelize(Seq(
      "humidity", "pressure", "temperature", "wind_direction", "wind_speed")
    ).foreach(src => {
      csvReader.readRawCsv(s"/$src/$basePathCurrentDate/$src.csv")
          .weatherToCitiesFormat().weatherToContinousGroups()
          .foreach(item => item._2
            .weatherWriteToSpecHdfs(s"/$src/$basePathCurrentDate/${item._1}")
            .writeToSpecMongoDb(s"${src}_${item._1}"))
      })
  }
}
