package com.jdfs.assessment.wheatheranalysis.features.process

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object CitiesDataFormatter {

  def citiesFormat(df: DataFrame): DataFrame = df
    .toDF("datetime", "Vancouver", "Portland", "San Francisco", "Seattle", "Los Angeles", "San Diego",
      "Las Vegas", "Phoenix", "Albuquerque", "Denver", "San Antonio", "Dallas", "Houston", "Kansas City", "Minneapolis",
      "Saint Louis", "Chicago", "Nashville", "Indianapolis", "Atlanta", "Detroit", "Jacksonville", "Charlotte", "Miami",
      "Pittsburgh", "Toronto", "Philadelphia", "New York", "Montreal", "Boston", "Beersheba", "Tel Aviv District", "Eilat",
      "Haifa", "Nahariyya", "Jerusalem")
    .select(col("datetime"), expr(Predef.augmentString(
      """stack(36,
        |'Vancouver',`Vancouver`,
        |'Portland',`Portland`,
        |'San Francisco',`San Francisco`,
        |'Seattle',`Seattle`,
        |'Los Angeles',`Los Angeles`,
        |'San Diego',`San Diego`,
        |'Las Vegas',`Las Vegas`,
        |'Phoenix',`Phoenix`,
        |'Albuquerque',`Albuquerque`,
        |'Denver',`Denver`,
        |'San Antonio',`San Antonio`,
        |'Dallas',`Dallas`,
        |'Houston',`Houston`,
        |'Kansas City',`Kansas City`,
        |'Minneapolis',`Minneapolis`,
        |'Saint Louis',`Saint Louis`,
        |'Chicago',`Chicago`,
        |'Nashville',`Nashville`,
        |'Indianapolis',`Indianapolis`,
        |'Atlanta',`Atlanta`,
        |'Detroit',`Detroit`,
        |'Jacksonville',`Jacksonville`,
        |'Charlotte',`Charlotte`,
        |'Miami',`Miami`,
        |'Pittsburgh',`Pittsburgh`,
        |'Toronto',`Toronto`,
        |'Philadelphia',`Philadelphia`,
        |'New York',`New York`,
        |'Montreal',`Montreal`,
        |'Boston',`Boston`,
        |'Beersheba',`Beersheba`,
        |'Tel Aviv District',`Tel Aviv District`,
        |'Eilat',`Eilat`,
        |'Haifa',`Haifa`,
        |'Nahariyya',`Nahariyya`,
        |'Jerusalem',`Jerusalem`
        |) as (country, value)""").stripMargin))
    .withColumn("date", split(col("datetime"), " ").getItem(0))
    .withColumn("month", date_format(to_date(col("datetime"), "yyyy-MM-dd HH:mm:ss"), "MMMM"))
    .withColumn("time", split(col("datetime"), " ").getItem(1))
    .withColumn("interval", when(col("time") <= "00:04:59", "00:00 to 00:04")
      .when(col("time") >= "00:05:00" && col("time") <= "00:07:59", "00:04 to 00:08")
      .when(col("time") >= "00:08:00" && col("time") <= "00:11:59", "00:08 to 00:12")
      .when(col("time") >= "00:12:00" && col("time") <= "15:59:59", "00:12 to 16:00")
      .when(col("time") >= "16:00:00" && col("time") <= "19:59:59", "16:00 to 20:00")
      .when(col("time") >= "20:00:00" && col("time") <= "23:59:59", "20:00 to 24:00"))

}
