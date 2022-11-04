sudo -u hdfs hadoop fs -mkdir -p /user/custom
sudo -u hdfs hadoop fs -chmod 777 /user/custom

mkdir /home/ec2-user/weatheranalysis/logs -p

setsid nohup /opt/flume/bin/flume-ng agent \
--conf-file /home/ec2-user/weatheranalysis/flume_setup/weather-ingestion-flume-agent.conf \
--name weather_agent \
--conf /opt/flume/conf/ -Xms1024m -Xmx2048m \
-Dflume.root.logger=INFO,console \
 1> /home/ec2-user/weatheranalysis/logs/flume.out_1 2> /home/ec2-user/weatheranalysis/logs/flume.out_2 < /dev/null &