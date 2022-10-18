package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.app.ports.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;

public interface PostTopicPort {

    /**
     * Sends data to kafka topic
     * @param data
     * @param topicName
     * @throws JsonProcessingException
     */
    void postTopic(WeatherOutput data, String topicName) throws JsonProcessingException;
}
