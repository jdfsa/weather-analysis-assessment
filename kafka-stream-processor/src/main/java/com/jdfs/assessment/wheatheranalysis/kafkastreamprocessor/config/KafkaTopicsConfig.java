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

    @Value("${app.source-topic-name}")
    private String publisherTopicName;

    @Value("#{${app.source-destination-stream-map}}")
    private Map<String, String> sourceTopicMapping;

    @Bean
    public KafkaAdmin.NewTopics kafkaTopics() {
        final List<NewTopic> topics = sourceTopicMapping.entrySet().stream()
                .map(item -> TopicBuilder.name(item.getValue()).partitions(3).build())
                .collect(Collectors.toList());
        topics.add(TopicBuilder.name(publisherTopicName).partitions(3).build());
        final NewTopic[] topicsArray = topics.toArray(new NewTopic[topics.size()]);
        return new KafkaAdmin.NewTopics(topicsArray);
    }

}
