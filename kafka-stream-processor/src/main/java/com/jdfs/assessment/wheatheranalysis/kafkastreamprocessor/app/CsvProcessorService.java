package com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.app;

import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.app.ports.in.ProcessCsvUseCase;
import com.jdfs.assessment.wheatheranalysis.kafkastreamprocessor.domain.WeatherOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
class CsvProcessorService implements ProcessCsvUseCase {

    @Override
    public void processCsvFile(final String filename, final String separator,
                               final Consumer<WeatherOutput> itemConsumer) throws IOException {

        final Path csvFilePath = Paths.get(URI.create(filename));
        try (
                BufferedReader reader = Files.newBufferedReader(csvFilePath)
        ) {
            final String[] csvHeader = reader.readLine().split(separator);
            String csvLine = null;
            while ((csvLine = reader.readLine()) != null) {
                final Map<String, String> lineContent = new HashMap<>();
                final String[] values = csvLine.split(separator);
                for (int i = 0; i < values.length; i++) {
                    lineContent.put(csvHeader[i], values[i]);
                }
                // final Publisher publisher = new Publisher(csvFilePath.toFile().getName(), lineContent);
                // itemConsumer.accept(publisher);
            }
        }
    }
}
