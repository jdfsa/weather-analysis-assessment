package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class WeatherOutput implements Serializable {

    private Map<String, String> data;

    @JsonIgnore
    private WeatherInput originalInput;
}
