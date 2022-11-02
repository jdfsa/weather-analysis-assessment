package com.jdfs.assessment.wheatheranalysis.features.out

import org.apache.http.client.utils.URIBuilder
import org.apache.spark.sql.{DataFrame, SaveMode}


case object MongoSpecsWriter {

  def writeSpecMongoDb(df: DataFrame, tableName: String): Unit = {
    val baseConnection = df.sparkSession.conf.get("weatherapp.mongodb.base_connection")
    val ssl = df.sparkSession.conf.get("weatherapp.mongodb.ssl")
    val tlsCaFile = df.sparkSession.conf.get("weatherapp.mongodb.tls_ca_file")
    val replicaSet = df.sparkSession.conf.get("weatherapp.mongodb.replicaSet")
    val readPreference = df.sparkSession.conf.get("weatherapp.mongodb.readPreference")
    val retryWrites = df.sparkSession.conf.get("weatherapp.mongodb.retryWrites")
    val uri = new URIBuilder(baseConnection + "." + tableName)
    if (ssl.nonEmpty) {
      uri.addParameter("ssl", ssl)
    }
    if (tlsCaFile.nonEmpty) {
      uri.addParameter(f"tlsCAFile", tlsCaFile)
    }
    if (replicaSet.nonEmpty) {
      uri.addParameter("replicaSet", replicaSet)
    }
    if (readPreference.nonEmpty) {
      uri.addParameter("readPreference", readPreference)
    }
    if (retryWrites.nonEmpty) {
      uri.addParameter("retryWrites", retryWrites)
    }
    val mongoConn = uri.build().toString
    println(f"MONGO CONNECTION: $mongoConn")
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
