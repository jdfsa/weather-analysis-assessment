package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.adapter.in;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherInput;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
class KafkaStreamProcessor {

    @Bean
    public Function<KStream<String, WeatherInput>, KStream<String, WeatherOutput>[]> weatherDataProcessor() {
        return kstream -> kstream
                .filter((key, data) -> )
    }

}
