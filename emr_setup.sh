# install kafka binaries
wget https://archive.apache.org/dist/kafka/2.6.1/kafka_2.12-2.6.1.tgz
tar xzf kafka_2.12-2.6.1.tgz
sudo mv -f kafka_2.12-2.6.1 /opt
sudo ln -s kafka_2.12-2.6.1 /opt/kafka
export KAFKA_HOME=/opt/kafka
export PATH=$PATH:${KAFKA_HOME}/bin
/opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/server.properties

# install flume binaries
wget https://dlcdn.apache.org/flume/1.11.0/apache-flume-1.11.0-bin.tar.gz
tar xzf apache-flume-1.11.0-bin.tar.gz
sudo mv -f apache-flume-1.11.0-bin /opt
sudo ln -s apache-flume-1.11.0-bin /opt/flume
sudo cp ./flume_setup/log4j2.xml /opt/flume/conf
export FLUME_HOME=/opt/flume
export PATH=$PATH:${FLUME_HOME}/bin

# configure ssl certificate to access documentdb cluster from spark
wget https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem
openssl x509 -outform der -in ./rds-ca-2019-root.pem -out ./rds-ca-2019-root.der
sudo keytool -import -alias rds-ca-2019 -keystore /etc/pki/java/cacerts -file ./rds-ca-2019-root.der -storepass changeit -noprompt

# configuring topics
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-publisher-data
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-city-attributes
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-humidity
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-pressure
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-temperature
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-description
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-wind-direction
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic weather-data-wind-speed
/opt/kafka/bin/kafka-topics.sh --zookeeper localhost:2181 --list | grep weather

# copy systemd services
sudo cp ./services/*.service /etc/systemd/system
sudo systemctl daemon-reload

# starting stream processor
sudo systemctl start kafkastreamprocessor.service
sudo systemctl status kafkastreamprocessor.service

# starting flume
sudo chmod u+x ./flume_setup/agent-run.sh
./flume_setup/agent-run.sh
