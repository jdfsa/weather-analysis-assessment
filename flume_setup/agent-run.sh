/opt/flume/bin/flume-ng agent \
--conf-file /home/ec2-user/weatheranalysis/flume_setup/weather-ingestion-flume-agent.conf \
--name weather_agent \
--conf /opt/flume/conf/ -Xms1024m -Xmx2048m \
-Dflume.root.logger=INFO,console