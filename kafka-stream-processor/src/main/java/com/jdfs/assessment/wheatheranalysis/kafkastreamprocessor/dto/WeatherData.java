package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Stream;

@Getter
public class WeatherData {
    private static String FIELD_SEPARATOR = ",";

    private final String[] data;
    private final String source;

    public WeatherData(final String plainTextData, final String source) {
        this.data = plainTextData.split(FIELD_SEPARATOR);
        this.source = source;
    }

    public Stream<String> getDataAsStream() {
        return Arrays.stream(data);
    }

    public String getDataAsPlainText() {
        return String.join(FIELD_SEPARATOR, data);
    }
}
