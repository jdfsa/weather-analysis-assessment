package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.FeaturesImplicits.Functions
import com.jdfs.assessment.wheatheranalysis.features.in.CsvReader.readRawCsv
import org.apache.spark.sql.SparkSession

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object App {

  val spark = SparkSession.builder
    .appName("WeatherAnalysisBatchProcessing")
    .master("spark://localhost:7077")
    .config("weatherapp.hdfs.base_path", "hdfs://127.0.0.1:9010/user/custom/weather_data")
    .config("weatherapp.mongodb.base_connection", s"mongodb://127.0.0.1:27017/weather")
    .getOrCreate

  def main(args: Array[String]): Unit = {

    val basePathCurrentDate = LocalDate.now.format(DateTimeFormatter.ofPattern(("yyyy/MM/dd")))

    // city attributes
    readRawCsv(s"/city_attributes/$basePathCurrentDate/")
      .toDF("City","Country","Latitude","Longitude")
      .writeToSpecHdfs(s"/city_attributes/$basePathCurrentDate/")
      .writeToSpecMongoDb("city_attributes")


    // weather description
    readRawCsv(s"/weather_description/$basePathCurrentDate/")
      .weatherToCitiesFormat()
      .weatherToDiscreteGroups()
      .foreach(item => item._2
        .writeToSpecHdfs(s"/weather_description/$basePathCurrentDate/${item._1}")
        .writeToSpecMongoDb(s"weather_description_${item._1}"))

    // other data
    spark.sparkContext.parallelize(Seq(
      "humidity", "pressure", "temperature", "wind_direction", "wind_speed")
    ).foreach(src => {
      readRawCsv(s"/$src/$basePathCurrentDate/")
        .weatherToCitiesFormat().weatherToContinousGroups()
        .foreach(item => item._2
          .writeToSpecHdfs(s"/$src/$basePathCurrentDate/${item._1}")
          .writeToSpecMongoDb(s"${src}_${item._1}"))
    })
  }
}
