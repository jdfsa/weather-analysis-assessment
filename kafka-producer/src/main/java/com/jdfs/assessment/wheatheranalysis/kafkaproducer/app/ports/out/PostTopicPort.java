package com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain.Publisher;

public interface PostTopicPort {
    void postTopic(Publisher data) throws JsonProcessingException;
}
