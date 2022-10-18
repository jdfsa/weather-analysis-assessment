package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class KafkaTopicsConfig {

    private final ApplicationContext context;

    @Value("${app.kafka-topics.source-topic-name}")
    private String publisherTopicName;

    @Value("#{${app.kafka-topics.source-destination-stream-map}}")
    private Map<String, String> sourceTopicMap;

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        final List<NewTopic> topics = sourceTopicMap.entrySet().stream()
                .map(item -> TopicBuilder.name(item.getValue()).partitions(3).build())
                .collect(Collectors.toList());
        topics.add(TopicBuilder.name(publisherTopicName).partitions(3).build());
        return new KafkaAdmin.NewTopics(topics.toArray(size -> new NewTopic[size]));
    }

}
