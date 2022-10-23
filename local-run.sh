docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-publisher-data --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-description --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-city-attributes --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-temperature --from-beginning
docker exec --interactive --tty broker kafka-console-consumer --bootstrap-server broker:9092 --topic weather-data-humidity

docker exec broker kafka-topics --bootstrap-server broker:9092 --delete --topic weather-publisher-data

"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/city_attributes.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/humidity.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/pressure.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/temperature.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/weather_description.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/wind_direction.csv
"C:\Users\jacqu\git\weather-analysis-assessment\kafka-producer\target\kafka-producer-0.0.1.jar" --separator=, --file=file:///C:/Users/jacqu/git/weather-analysis-assessment/dataset/wind_speed.csv



# emr
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic weather-data-city-attributes --from-beginning
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic weather-data-temperature --from-beginning
