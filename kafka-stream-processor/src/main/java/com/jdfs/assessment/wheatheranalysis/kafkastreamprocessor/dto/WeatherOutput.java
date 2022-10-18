package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class WeatherOutput implements Serializable {

    private final Map<String, String> data;

    @JsonIgnore
    private final WeatherInput originalInput;
}
