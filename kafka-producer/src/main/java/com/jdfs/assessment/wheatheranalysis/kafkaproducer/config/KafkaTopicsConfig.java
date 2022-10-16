package com.jdfs.assessment.wheatheranalysis.kafkaproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig {

    @Value("${app.kafka-topic.publisher.name}")
    private String publisherTopicName;

    @Bean
    public NewTopic publisherTopic() {
        return TopicBuilder.name(publisherTopicName).build();
    }
}
