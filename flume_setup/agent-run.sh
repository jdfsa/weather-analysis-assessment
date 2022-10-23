sudo -u hdfs hadoop fs -mkdir /user/jdfs
sudo -u hdfs hadoop fs -chmod 777 /user/jdfs

/opt/flume/bin/flume-ng agent \
--conf-file /home/ec2-user/weatheranalysis/flume_setup/city_attributes_agent.conf \
--name city_attributes_agent \
--conf /opt/flume/conf/ \
-Dflume.root.logger=DEBUG,console