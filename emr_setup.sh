# installation
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

# copy systemd services
sudo cp ./services/*.service /etc/systemd/system
sudo systemctl daemon-reload

# starting stream processor
sudo systemctl start kafkastreamprocessor.service
sudo systemctl status kafkastreamprocessor.service

# starting flume TO DO
sudo chmod u+x /home/ec2-user/weatheranalysis/flume_setup/agent-run.sh
/home/ec2-user/weatheranalysis/flume_setup/agent-run.sh
#sudo -u hdfs hadoop fs -mkdir -p /user/custom
#sudo -u hdfs hadoop fs -chmod 777 /user/custom
#sudo systemctl start weather-ingestion-flume-agent.service
#sudo systemctl status weather-ingestion-flume-agent.service
