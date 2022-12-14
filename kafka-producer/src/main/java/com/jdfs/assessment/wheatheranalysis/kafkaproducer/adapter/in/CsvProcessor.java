package com.jdfs.assessment.wheatheranalysis.kafkaproducer.adapter.in;

import com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.in.ProcessCsvUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CsvProcessor implements ApplicationRunner {

    private final ProcessCsvUseCase processCsvUseCase;

    @Override
    public void run(ApplicationArguments args) {
        final String csvFileName = args.getOptionValues("file").get(0);
        final String csvSeparator = args.getOptionValues("separator").get(0);

        try {
            processCsvUseCase.processCsvFile(csvFileName, csvSeparator);
        }
        catch (final Exception e) {
            log.error("Falha no processamento do arquivo", e);
        }
    }
}
