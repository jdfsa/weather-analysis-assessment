package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.adapter;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherInput;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
class KafkaStreamProcessor {

    private static String FIELD_SEPARATOR = ",";

    private final StreamsBuilder streamsBuilder;

    @PostConstruct
    public void streamTopoloty() {
        final var streamMap = streamsBuilder.stream("weather-publisher-data",
                        Consumed.with(Serdes.String(), new JsonSerde<>(WeatherInput.class)))
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
                });

        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "city_attributes"))
                .to("weather-data-city-attributes", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "humidity"))
                .to("weather-data-humidity", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "pressure"))
                .to("weather-data-pressure", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "temperature"))
                .to("weather-data-temperature", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "weather_description"))
                .to("weather-data-description", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "wind_direction"))
                .to("weather-data-wind-direction", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        streamMap.filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), "wind_speed"))
                .to("weather-data-wind-speed", Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
    }

}
