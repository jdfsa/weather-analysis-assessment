package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.app;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto.WeatherData;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto.WeatherInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
class KafkaStreamProcessor {

    private final StreamsBuilder streamsBuilder;

    @Value("${app.source-topic-name}")
    private String sourceTopic;

    @Value("#{${app.source-destination-stream-map}}")
    private Map<String, String> sourceDestinationMapping;

    @PostConstruct
    public void streamTopoloty() {

        // builds an stream
        final var streamMap = streamsBuilder
                // use JsonSerde to parse input data
                .stream(sourceTopic, Consumed.with(Serdes.String(), new JsonSerde<>(WeatherInput.class)))
                // map values to output format
                .mapValues((key, value) -> new WeatherData(value.getContent(), value.getSource()))
                // skip items having empty fields
                .filter((key, value) -> value.getDataAsStream().allMatch(data -> StringUtils.isNotBlank(data)));

        // determines the target for each stream
        for (final Map.Entry<String, String> destinationEntry : sourceDestinationMapping.entrySet()) {
            streamMap
                    // map to the correct destination
                    .filter((key, output) -> Objects.equals(output.getSource(), destinationEntry.getKey()))
                    // drop headers and convert to csv format
                    .mapValues((key, output) -> output.getDataAsPlainText())
                    // determine the destination topic
                    .to(destinationEntry.getValue());
        }
    }

}
