package com.jdfs.assessment.wheatheranalysis.features

import com.jdfs.assessment.wheatheranalysis.features.out.HdfsSpecsWriter.writeSpec
import com.jdfs.assessment.wheatheranalysis.features.process.CitiesDataFormatter.citiesFormat
import com.jdfs.assessment.wheatheranalysis.features.process.ContinousSegmentsSplitter.groupedContinousData
import com.jdfs.assessment.wheatheranalysis.features.process.DiscreteSegmentsSplitter.groupedDiscreteData
import org.apache.spark.sql.DataFrame

object FeaturesImplicits {
  implicit class Functions(df: DataFrame) {
    def weatherToCitiesFormat(): DataFrame = citiesFormat(df)
    def weatherToContinousGroups(): Map[String, DataFrame] = groupedContinousData(df)
    def weatherToDiscreteGroups(): Map[String, DataFrame] = groupedDiscreteData(df)
    def weatherWriteSpecData(path: String): Unit = writeSpec(df, path)
  }
}
