package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.adapter;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherInput;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.source-topic-name}")
    private String sourceTopic;

    @Value("#{${app.source-destination-stream-map}}")
    private Map<String, String> sourceDestinationMapping;

    @PostConstruct
    public void streamTopoloty() {
        final var streamMap = streamsBuilder
                .stream(sourceTopic, Consumed.with(Serdes.String(), new JsonSerde<>(WeatherInput.class)))
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

        for (final Map.Entry<String, String> entry : sourceDestinationMapping.entrySet()) {
            streamMap
                    .filter((key, output) -> Objects.equals(output.getOriginalInput().getSource(), entry.getKey()))
                    .to(entry.getValue(), Produced.with(Serdes.String(), new JsonSerde<>(WeatherOutput.class)));
        }
    }

}
