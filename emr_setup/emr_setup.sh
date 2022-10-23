# https://www.alibabacloud.com/help/en/e-mapreduce/latest/synchronize-data-from-an-emr-kafka-cluster-to-oss
# https://normanlimxk.com/2021/11/01/setup-a-kafka-cluster-on-amazon-ec2/

# temp variables
S3_BUCKET_NAME=weatheranalysistransient

# installation

mkdir weatheranalysis
cd weatheranalysis

wget https://archive.apache.org/dist/kafka/2.6.1/kafka_2.12-2.6.1.tgz
tar xzf kafka_2.12-2.6.1.tgz
sudo mv -f kafka_2.12-2.6.1 /opt
sudo ln -s kafka_2.12-2.6.1 /opt/kafka
export KAFKA_HOME=/opt/kafka
export PATH=$PATH:${KAFKA_HOME}/bin
/opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties

wget https://dlcdn.apache.org/flume/1.10.1/apache-flume-1.10.1-bin.tar.gz
tar xzf apache-flume-1.10.1-bin.tar.gz
sudo mv -f apache-flume-1.10.1-bin /opt
sudo ln -s apache-flume-1.10.1-bin /opt/flume
export FLUME_HOME=/opt/flume
export PATH=$PATH:${FLUME_HOME}/bin

# configuring topics
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

# starting stream processor
sudo cp ./emr_setup/*.service /etc/systemd/system
sudo systemctl daemon-reload
sudo systemctl start kafkastreamprocessor.service
sudo systemctl status kafkastreamprocessor.service

# starting flume TO DO
sudo -u hdfs hadoop fs -mkdir -p /user/custom
sudo -u hdfs hadoop fs -chmod 777 /user/custom
sudo cp ./flume_setup/*.service /etc/systemd/system
sudo systemctl start weather-investion-flume-agent.service
sudo systemctl status weather-investion-flume-agent.service
# chmod u+x ./flume_setup/agent-run.sh
# ./flume_setup/agent-run.sh

# starting producer
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/city_attributes.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/humidity.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/pressure.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/temperature.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/weather_description.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/wind_direction.csv
java -jar ./kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/wind_speed.csv
