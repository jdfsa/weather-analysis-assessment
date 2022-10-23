sudo -u hdfs hadoop fs -mkdir /user/custom
sudo -u hdfs hadoop fs -chmod 777 /user/custom

/opt/flume/bin/flume-ng agent \
--conf-file /home/ec2-user/weatheranalysis/flume_setup/weather-investion-flume-agent.conf \
--name weather_agent \
--conf /opt/flume/conf/ -Xms1024m -Xmx2048m \
-Dflume.root.logger=DEBUG,console