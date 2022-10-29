package com.jdfs.assessment.wheatheranalysis.features

import com.jdfs.assessment.wheatheranalysis.features.out.HdfsSpecsWriter.writeSpec
import com.jdfs.assessment.wheatheranalysis.features.process.CitiesDataFormatter.citiesFormat
import com.jdfs.assessment.wheatheranalysis.features.process.SegmentsSplitter.groupedData
import org.apache.spark.sql.DataFrame

object FeaturesImplicits {
  implicit class Functions(df: DataFrame) {
    def weatherCitiesFormat(): DataFrame = citiesFormat(df)
    def weatherSplitGroups(): Map[String, DataFrame] = groupedData(df)
    def weatherSaveSpecData(path: String): Unit = writeSpec(df, path)
  }
}
