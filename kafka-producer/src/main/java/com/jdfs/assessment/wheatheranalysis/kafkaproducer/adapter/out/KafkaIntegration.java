package com.jdfs.assessment.wheatheranalysis.kafkaproducer.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.out.PostTopicPort;
import com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain.Publisher;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class KafkaIntegration implements PostTopicPort {

    @Value("${app.kafka-topic.publisher.name}")
    private String publisherTopicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void postTopic(final Publisher data) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String content = mapper.writeValueAsString(data);
        final ProducerRecord<String, String> record = new ProducerRecord<>(
                publisherTopicName, UUID.randomUUID().toString(), content
        );
        kafkaTemplate.send(record);
    }
}
