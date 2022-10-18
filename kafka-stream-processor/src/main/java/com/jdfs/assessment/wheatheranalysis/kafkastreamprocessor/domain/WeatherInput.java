package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WeatherInput implements Serializable {
    private String source;
    private String headers;
    private String content;
}
