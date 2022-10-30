package com.jdfs.assessment.wheatheranalysis.features.out

import org.apache.spark.sql.{DataFrame, SaveMode}


case object MongoSpecsWriter {

  def writeSpecMongoDb(df: DataFrame, tableName: String): Unit = {
    val mongoConn = s"${df.sparkSession.conf.get("weatherapp.mongodb.base_connection")}.$tableName"
    df.write.format("com.mongodb.spark.sql.DefaultSource")
      .option("spark.mongodb.output.uri", mongoConn)
      .option("spark.mongodb.operationType", "replace")
      .option("spark.mongodb.idFieldList", fieldListByTableName(tableName))
      .mode(SaveMode.Append)
      .save()
  }

  private def fieldListByTableName(tableName: String): String =
    tableName.split("_").last match {
      case "monthly" => "city,month"
      case "daily" => "city,date"
      case "interval" => "city,interval"
      case default => "city"
    }
}
