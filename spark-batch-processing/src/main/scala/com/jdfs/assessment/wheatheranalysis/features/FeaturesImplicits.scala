package com.jdfs.assessment.wheatheranalysis.features

import com.jdfs.assessment.wheatheranalysis.features.in.ArgsMapper.arrayToMap
import com.jdfs.assessment.wheatheranalysis.features.out.HdfsSpecsWriter.writeSpecHdfs
import com.jdfs.assessment.wheatheranalysis.features.out.MongoSpecsWriter.writeSpecMongoDb
import com.jdfs.assessment.wheatheranalysis.features.process.CitiesDataFormatter.citiesFormat
import com.jdfs.assessment.wheatheranalysis.features.process.ContinousSegmentsSplitter.groupedContinousData
import com.jdfs.assessment.wheatheranalysis.features.process.DiscreteSegmentsSplitter.groupedDiscreteData
import org.apache.spark.sql.DataFrame

object FeaturesImplicits {

  implicit class DataFrameFunctions(df: DataFrame) {
    def weatherToCitiesFormat(): DataFrame = citiesFormat(df)
    def weatherToContinousGroups(): Map[String, DataFrame] = groupedContinousData(df)
    def weatherToDiscreteGroups(): Map[String, DataFrame] = groupedDiscreteData(df)
    def weatherWriteToSpecHdfs(path: String): DataFrame = {
      writeSpecHdfs(df, path)
      df
    }
    def writeToSpecMongoDb(tableName: String): DataFrame = {
      writeSpecMongoDb(df, tableName)
      df
    }
  }

  implicit class ArrayFunctions(args: Array[String]) {
    def weatherToMapArgs(): Map[String, String] = arrayToMap(args)
  }
}
