package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class WeatherOutput implements Serializable {
    private final Map<String, String> data;
}
