package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInput implements Serializable {
    private String source;
    private String headers;
    private String content;
}
