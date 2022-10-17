package com.jdfs.assessment.wheatheranalysis.kafkaproducer.app;

import com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.in.ProcessCsvUseCase;
import com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.out.PostTopicPort;
import com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain.Publisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
class CsvProcessorService implements ProcessCsvUseCase {

    private final PostTopicPort postTopicPort;

    @Override
    public void processCsvFile(final String filename, final String separator) throws IOException {

        final Path csvFilePath = Paths.get(URI.create(filename));
        try (
                BufferedReader reader = Files.newBufferedReader(csvFilePath)
        ) {
            final String sourceName = csvFilePath.toFile().getName().replace(".csv", "");
            final String csvHeader = reader.readLine();
            String csvLine = null;
            while ((csvLine = reader.readLine()) != null) {
                final Publisher publisher = new Publisher(sourceName, csvHeader, csvLine);
                postTopicPort.postTopic(publisher);
            }
        }
    }
}
