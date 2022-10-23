S3_BUCKET_NAME=weatheranalysistransient
aws s3 cp ./kafka-producer/target/kafka-producer-0.0.1.jar s3://$S3_BUCKET_NAME
aws s3 cp ./kafka-stream-processor/target/kafka-stream-processor-0.0.1.jar s3://$S3_BUCKET_NAME
aws s3 cp ./flume/agent-kafka-hdfs.conf s3://$S3_BUCKET_NAME
aws s3 cp --recursive ./dataset/ s3://$S3_BUCKET_NAME/dataset/
aws s3 cp --recursive ./emr_setup/ s3://$S3_BUCKET_NAME/emr_setup/
aws s3 cp --recursive ./flume_setup/ s3://$S3_BUCKET_NAME/flume_setup/