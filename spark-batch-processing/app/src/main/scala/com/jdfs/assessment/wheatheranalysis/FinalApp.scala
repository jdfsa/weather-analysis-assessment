package com.jdfs.assessment.wheatheranalysis

import org.apache.spark.sql.SparkSession

import java.time.LocalDate
import java.time.format.DateTimeFormatter


object FinalApp {

  val BASE_CSV_RAW_PATH = "hdfs://127.0.0.1:9010/user/custom/weather_data"
//  val BASE_CSV_RAW_PATH = "hdfs://namenode:9000/user/custom/weather_data"
//  val BASE_CSV_RAW_PATH = "file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset-hadoop"

  val spark = SparkSession.builder
    .master("spark://localhost:7077")
//    .config("spark.network.timeout","60s")
//    .config("spark.executor.heartbeatInterval", "10s")
    .appName("WeatherAnalysisBatchProcessing")
    .getOrCreate

//  def readCsv(path: String, colNames: List[String]): DataFrame = {
//    val data = spark.read.option("header", "false").option("charset", "UTF-8")
//      .csv(s"$BASE_CSV_RAW_PATH/$path")
//      .toDF(colNames.toArray)
//
//    return data
//  }

  def main(args: Array[String]): Unit = {

    val basePathCurrentDate = LocalDate.now.minusDays(2)
      .format(DateTimeFormatter.ofPattern(("yyyy/MM/dd")))

    val cityAttributesDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/city_attributes/$basePathCurrentDate")
      .toDF("City","Country","Latitude","Longitude")
    cityAttributesDf.show(10)

    val humidityDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/humidity/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")

    val pressureDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/pressure/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")

    val temperatureDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/temperature/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")

    val weatherDescriptionDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/weather_description/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")

    val windDirectionDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/wind_direction/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")

    val windSpeedDf = spark.read.option("header", "false").option("charset", "UTF-8")
      .csv(s"$BASE_CSV_RAW_PATH/raw/wind_speed/$basePathCurrentDate")
      .toDF("datetime","Vancouver","Portland","San Francisco","Seattle","Los Angeles","San Diego",
        "Las Vegas","Phoenix","Albuquerque","Denver","San Antonio","Dallas","Houston","Kansas City","Minneapolis",
        "Saint Louis","Chicago","Nashville","Indianapolis","Atlanta","Detroit","Jacksonville","Charlotte","Miami",
        "Pittsburgh","Toronto","Philadelphia","New York","Montreal","Boston","Beersheba","Tel Aviv District","Eilat",
        "Haifa","Nahariyya","Jerusalem")
  }
}
