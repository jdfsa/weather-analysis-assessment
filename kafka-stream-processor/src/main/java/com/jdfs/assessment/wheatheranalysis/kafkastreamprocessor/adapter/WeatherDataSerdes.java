package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.adapter;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherDataSerdes extends Serdes {
//    private final static Map<String, String> serdeConfig = Stream.of(
//            new AbstractMap.SimpleEntry<>(FAIL_INVALID_SCHEMA, "true"),
//            new AbstractMap.SimpleEntry<>(JSON_VALUE_TYPE, WeatherOutput.class.getName())
//    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//    public static Serde<WeatherOutput> weatherDataOutputSerdes() {
//        final Serde<WeatherOutput> notificationSerde = new KafkaJsonSchemaSerde<>();
//        notificationSerde.configure(serdeConfig, false);
//        return notificationSerde;
//    }
}
