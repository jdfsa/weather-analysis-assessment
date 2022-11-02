# spark on docker
# https://github.com/Marcel-Jan/docker-hadoop-spark
# https://dev.to/mvillarrealb/creating-a-spark-standalone-cluster-with-docker-and-docker-compose-2021-update-6l4


# docker mongo db -----------
docker pull mongo:latest
docker run -d -p 27017:27017 --name mongodb mongo:latest
docker exec -it mongodb mongosh

# spark-batch-processing
export AWS_ACCESS_KEY_ID=XXXXXXXXXXXXXX
export AWS_SECRET_ACCESS_KEY=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
cd /mnt/c/Users/jacqu/git/weather-analysis-assessment
spark-submit --class com.jdfs.assessment.wheatheranalysis.App ./spark-batch-processing/target/sparkbatchprocessing-0.0.1-jar-with-dependencies.jar \
--spark_master=local[*] \
--weatherapp.hdfs.base_path=file:///mnt/c/Users/jacqu/git/weather-analysis-assessment/dataset-hadoop \
--weatherapp.s3.base_path=s3a://weatheranalysisdata \
--weatherapp.mongodb.base_connection=mongodb://127.0.0.1:27017/weather \
--weatherapp.reference_date=2022-10-26



# hdfs: preparing hadoop docker ----------------
docker cp dataset-hadoop namenode:/home/dataset
docker exec -it namenode bash
cd /home/dataset
hdfs dfs -copyFromLocal ./ /user/custom/weather_data/raw/
hdfs dfs -ls /user/custom/weather_data/raw/

docker cp ./target/sparkbatchprocessing-0.0.1.jar namenode:/home
docker exec -t namenode hdfs dfs -copyFromLocal /home/sparkbatchprocessing-0.0.1.jar /home
docker exec -it spark-master /spark/bin/spark-submit --class App --deploy-mode cluster --master spark://spark-master:7077 hdfs://nodename:9000/home/sparkbatchprocessing-0.0.1.jar

# ------------------

# spark ------------
docker exec -it spark-masterbash
spark/bin/spark-shell --master spark://spark-master:7077
# ------------------


# kafka ingestion --------------------------

docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-publisher-data --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-description --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-city-attributes --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-temperature --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-humidity

docker exec broker kafka-topics --bootstrap-server broker:9092 --delete --topic weather-publisher-data

"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/city_attributes.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/humidity.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/pressure.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/temperature.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/weather_description.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/wind_direction.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/wind_speed.csv



# emr
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic weather-data-city-attributes --from-beginning
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic weather-data-temperature --from-beginning
