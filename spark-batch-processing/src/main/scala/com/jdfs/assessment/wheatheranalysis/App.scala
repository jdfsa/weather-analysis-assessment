package com.jdfs.assessment.wheatheranalysis

import com.jdfs.assessment.wheatheranalysis.features.FeaturesImplicits.{ArrayFunctions, DataFrameFunctions}
import com.jdfs.assessment.wheatheranalysis.features.in.CsvReader
import org.apache.spark.sql.SparkSession

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object App {

  def main(args: Array[String]): Unit = {
    val argsMap = args.weatherToMapArgs()
    val spark = SparkSession.builder
      .appName("WeatherAnalysisBatchProcessing")
      .master(argsMap.get("spark_master").get)
      .config("weatherapp.hdfs.base_path", argsMap.get("weatherapp.hdfs.base_path").get)
      .config("weatherapp.s3.base_path", argsMap.get("weatherapp.s3.base_path").get)
      .config("weatherapp.mongodb.base_connection", argsMap.get("weatherapp.mongodb.base_connection").get)
      .config("weatherapp.mongodb.ssl", argsMap.get("weatherapp.mongodb.ssl").getOrElse(""))
      .config("weatherapp.mongodb.tls_ca_file", argsMap.get("weatherapp.mongodb.tls_ca_file").getOrElse(""))
      .config("weatherapp.mongodb.replicaSet", argsMap.get("weatherapp.mongodb.replicaSet").getOrElse(""))
      .config("weatherapp.mongodb.readPreference", argsMap.get("weatherapp.mongodb.readPreference").getOrElse(""))
      .config("weatherapp.mongodb.retryWrites", argsMap.get("weatherapp.mongodb.retryWrites").getOrElse(""))
      .config("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate

    val processOnly = (
      if (argsMap.get("weatherapp.process_only").isEmpty) Array[String]()
      else argsMap.get("weatherapp.process_only").get.split(",")
      ).toList

    val csvReader = new CsvReader(spark)

    val basePathCurrentDate = argsMap.get("weatherapp.reference_date")
      .getOrElse(LocalDate.now.format(DateTimeFormatter.ofPattern(("yyyy-MM-dd"))))
      .replace("-", "/")

    // city attributes
    if (processOnly.isEmpty || processOnly.contains("city_attributes")) {
      csvReader.readRawCsv(s"/city_attributes/$basePathCurrentDate/")
        .toDF("City", "Country", "Latitude", "Longitude")
        .weatherWriteToSpecHdfs(s"/city_attributes/$basePathCurrentDate/")
        .writeToSpecMongoDb("city_attributes")
    }

    // weather description
    if (processOnly.isEmpty || processOnly.contains("weather_description")) {
      csvReader.readRawCsv(s"/weather_description/$basePathCurrentDate/")
        .weatherToCitiesFormat()
        .weatherToDiscreteGroups()
        .foreach(item => item._2
          .weatherWriteToSpecHdfs(s"/weather_description/$basePathCurrentDate/${item._1}")
          .writeToSpecMongoDb(s"weather_description_${item._1}"))
    }

    // other data
    val seqProcess = List("humidity", "pressure", "temperature", "wind_direction", "wind_speed")
      .filter(item => processOnly.isEmpty || processOnly.contains(item))
    seqProcess.foreach(src => {
      csvReader.readRawCsv(s"/$src/$basePathCurrentDate/")
        .weatherToCitiesFormat().weatherToContinousGroups()
        .foreach(item => item._2
          .weatherWriteToSpecHdfs(s"/$src/$basePathCurrentDate/${item._1}")
          .writeToSpecMongoDb(s"${src}_${item._1}"))
    })
  }

}
