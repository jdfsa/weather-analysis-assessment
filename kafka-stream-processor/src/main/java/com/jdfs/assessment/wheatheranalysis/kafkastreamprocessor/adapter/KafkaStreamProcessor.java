package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.adapter;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherInput;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
class KafkaStreamProcessor {

    private static String FIELD_SEPARATOR = ",";

    @Bean("weatherProcessor")
    public Function<KStream<String, WeatherInput>, KStream<String, WeatherOutput>[]> weatherProcessor() {
        return kstream -> {
            var streamMap = kstream
                    .mapValues((key, value) -> {
                        final String[] headers = value.getHeaders().split(FIELD_SEPARATOR);
                        final Map<String, String> data = new HashMap<>();
                        final String[] values = value.getContent().split(FIELD_SEPARATOR);
                        for (int i = 0; i < values.length; i++) {
                            data.put(headers[i], values[i]);
                        }

                        final WeatherOutput output = new WeatherOutput();
                        output.setData(data);
                        output.setOriginalInput(value);
                        return output;
                    })
                    .split()
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "city_attributes"),
                            Branched.as("output-weather-data-city-attributes"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "humidity"),
                            Branched.as("output-weather-data-humidity"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "pressure"),
                            Branched.as("output-weather-data-pressure"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "temperature"),
                            Branched.as("output-weather-data-temperature"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "weather_description"),
                            Branched.as("output-weather-data-description"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "wind_direction"),
                            Branched.as("output-weather-data-wind-direction"))
                    .branch((key, output) -> Objects.equals(output.getOriginalInput(), "wind_speed"),
                            Branched.as("output-weather-data-wind-speed"))
                    .noDefaultBranch();

            for (final Map.Entry<String, KStream<String, WeatherOutput>> entry : streamMap.entrySet()) {
                entry.getValue().to(entry.getKey());
            }

            return streamMap.values().stream().toArray(KStream[]::new);
        };
    }

}
