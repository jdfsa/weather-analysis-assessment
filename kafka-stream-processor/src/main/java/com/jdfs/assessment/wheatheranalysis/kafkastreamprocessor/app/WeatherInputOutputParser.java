package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.app;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto.WeatherInput;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.dto.WeatherOutput;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherInputOutputParser {

    private static String FIELD_SEPARATOR = ",";

    /**
     * Parses weather input into output data format
     * @param input
     * @return
     */
    public WeatherOutput parse(final WeatherInput input) {

        // split headers by separator
        final String[] headers = input.getHeaders().split(FIELD_SEPARATOR);

        // split values by separator and combines with headers to build a key/value structure
        final Map<String, String> data = new HashMap<>();
        final String[] values = input.getContent().split(FIELD_SEPARATOR);
        for (int i = 0; i < values.length; i++) {
            data.put(headers[i], values[i]);
        }

        // build output object, keeps original input if needed
        return new WeatherOutput(data, input);
    }
}
