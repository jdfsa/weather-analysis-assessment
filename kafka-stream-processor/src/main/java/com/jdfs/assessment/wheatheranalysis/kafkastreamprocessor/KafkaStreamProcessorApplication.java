package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableKafkaStreams
@SpringBootApplication
public class KafkaStreamProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamProcessorApplication.class, args);
	}

}
