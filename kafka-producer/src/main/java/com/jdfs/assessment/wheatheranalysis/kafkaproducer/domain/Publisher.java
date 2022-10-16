package com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Publisher implements Serializable {
    private final String filename;
    private final Map<String, String> data;
}
