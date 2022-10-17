package com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class Publisher implements Serializable {
    private final String source;
    private final String headers;
    private final String content;
}
