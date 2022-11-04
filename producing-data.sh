# local environment variables
S3_TRANSIENT_BUCKET_NAME=weatheranalysistransient
S3_DATA_DATA_BUCKET_NAME=weatheranalysisdata
MONGODB_ENDPOINT=mongodb://root:root2022@weather-analysis-documentdb.cluster-c4se9etyjkgh.us-east-1.docdb.amazonaws.com:27017/weather
HDFS_HOSTNAME_PORT=${HOSTNAME}.ec2.internal:8020
REFERENCE_DATE=2022-11-04

# download setup files from s3
mkdir /home/ec2-user/weatheranalysis
mkdir /home/ec2-user/weatheranalysis/logs
cd /home/ec2-user/weatheranalysis
aws s3 cp --recursive s3://$S3_TRANSIENT_BUCKET_NAME ./

# setup emr
sed -i 's/\r$//' ./emr_setup.sh
sudo chmod u+x ./emr_setup.sh
./emr_setup.sh

# starting producer
cd /home/ec2-user/weatheranalysis
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/city_attributes.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/humidity.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/pressure.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/temperature.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/weather_description.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/wind_direction.csv
java -jar ./java/kafka-producer-0.0.1.jar --separator=, --file=file:///home/ec2-user/weatheranalysis/dataset/wind_speed.csv

# starting batch processor
nohup sudo spark-submit \
./java/sparkbatchprocessing-0.0.1-jar-with-dependencies.jar \
--spark_master=local[*] \
--weatherapp.hdfs.base_path=hdfs://$HDFS_HOSTNAME_PORT/user/custom/weather_data \
--weatherapp.s3.base_path=s3a://$S3_DATA_DATA_BUCKET_NAME \
--weatherapp.mongodb.base_connection=$MONGODB_ENDPOINT \
--weatherapp.mongodb.ssl=true \
--weatherapp.mongodb.replicaSet=rs0 \
--weatherapp.mongodb.readPreference=secondaryPreferred \
--weatherapp.mongodb.retryWrites=false \
--weatherapp.reference_date=$REFERENCE_DATE \
--weatherapp.process_only=city_attributes,weather_description,humidity,pressure,temperature,wind_direction,wind_speed \
1> ./logs/sparkbatchprocessing.out_1 2> ./logs/sparkbatchprocessing.out_2 < /dev/null &
