package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.FeaturesImplicits.Functions
import com.jdfs.assessment.wheatheranalysis.features.in.CsvReader.readRawCsv
import org.apache.spark.sql.SparkSession

object LocalApp {

  val spark = SparkSession.builder
    .appName("WeatherAnalysisBatchProcessing")
    .master("local[*]")
    .config("spark.mongodb.input.uri", "mongodb://hduser:bigdata@127.0.0.1:27017/weather_data")
    .config("spark.mongodb.output.uri", "mongodb://hduser:bigdata@127.0.0.1:27017/weather_data")
    .getOrCreate

  def main(args: Array[String]): Unit = {

    val basePathCurrentDate = "2022/10/26"

    // city attributes
    readRawCsv(s"/city_attributes/$basePathCurrentDate/city_attributes.csv")
      .toDF("City","Country","Latitude","Longitude")
      .writeToSpecHdfs(s"/city_attributes/$basePathCurrentDate/")
      .writeToSpecMongoDb("city_attributes")


    // weather description
    readRawCsv(s"/weather_description/$basePathCurrentDate/weather_description.csv")
      .weatherToCitiesFormat()
      .weatherToDiscreteGroups()
      .foreach(item => item._2
        .writeToSpecHdfs(s"/weather_description/$basePathCurrentDate/${item._1}")
        .writeToSpecMongoDb(s"weather_description_${item._1}"))

    // other data
    spark.sparkContext.parallelize(Seq(
      "humidity", "pressure", "temperature", "wind_direction", "wind_speed")
    ).foreach(src => {
        readRawCsv(s"/$src/$basePathCurrentDate/$src.csv")
          .weatherToCitiesFormat().weatherToContinousGroups()
          .foreach(item => item._2
            .writeToSpecHdfs(s"/$src/$basePathCurrentDate/${item._1}")
            .writeToSpecMongoDb(s"${src}_${item._1}"))
      })
  }
}
