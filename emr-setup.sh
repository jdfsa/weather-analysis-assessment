# https://www.alibabacloud.com/help/en/e-mapreduce/latest/synchronize-data-from-an-emr-kafka-cluster-to-oss
# https://normanlimxk.com/2021/11/01/setup-a-kafka-cluster-on-amazon-ec2/

# temp variables
S3_BUCKET_NAME=weatheranalysistransient


# installation

mkdir weatheranalysis
cd weatheranalysis

sudo yum install -y git

wget https://archive.apache.org/dist/kafka/2.6.1/kafka_2.12-2.6.1.tgz
tar xzf kafka_2.12-2.6.1.tgz
sudo mv -f kafka_2.12-2.6.1 /opt
sudo ln -s kafka_2.12-2.6.1 /opt/kafka
export KAFKA_HOME=/opt/kafka
export PATH=$PATH:${KAFKA_HOME}/bin
/opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties

# configuring
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-publisher-data
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-city-attributes
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-humidity
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-pressure
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-temperature
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-description
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-wind-direction
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-wind-speed


# downloading content
aws s3 cp --recursive s3://$S3_BUCKET_NAME ./



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



copy "C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" "C:\Users\jacqu\git\weather-analysis-assessment\compiled"
copy "C:\Users\jacqu\git\weather-analysis-assessment\kafka-stream-processor\target\kafka-stream-processor-0.0.1.jar" "C:\Users\jacqu\git\weather-analysis-assessment\compiled"