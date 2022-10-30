package com.jdfs.assessment.wheatheranalysis.features.in

case object ArgsMapper {
    def arrayToMap(args: Array[String]): Map[String, String] =
      args.map(arg => {
        val values = arg.stripPrefix("--").split("=")
        values(0) -> values(1)
      }).toMap
}
