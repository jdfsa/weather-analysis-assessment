# https://www.alibabacloud.com/help/en/e-mapreduce/latest/synchronize-data-from-an-emr-kafka-cluster-to-oss
# https://normanlimxk.com/2021/11/01/setup-a-kafka-cluster-on-amazon-ec2/
export AWS_ACCESS_KEY_ID=XXXXXXXXXXXXXX
export AWS_SECRET_ACCESS_KEY=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
S3_TRANSIENT_BUCKET_NAME=weatheranalysistransient
S3_DATA_DATA_BUCKET_NAME=weatheranalysisdata

mkdir /home/ec2-user/weatheranalysis
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
spark-submit ./java/sparkbatchprocessing-0.0.1-jar-with-dependencies.jar \
--spark_master=spark://localhost:7077 \
--weatherapp.hdfs.base_path=hdfs://127.0.0.1:9010/user/custom/weather_data \
--weatherapp.s3.base_path=s3a://$S3_DATA_DATA_BUCKET_NAME \
--weatherapp.mongodb.base_connection=mongodb://127.0.0.1:27017/weather \
--weatherapp.reference_date=2022-10-26
