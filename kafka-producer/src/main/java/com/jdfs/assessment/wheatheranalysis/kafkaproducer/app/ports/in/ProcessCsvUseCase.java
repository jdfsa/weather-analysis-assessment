package com.jdfs.assessment.wheatheranalysis.kafkaproducer.app.ports.in;

import com.jdfs.assessment.wheatheranalysis.kafkaproducer.domain.Publisher;

import java.io.IOException;
import java.util.function.Consumer;

public interface ProcessCsvUseCase {

    /**
     * Reads a CSV File and invoke the itemConsumer for each record
     * @param filename
     * @param separator
     */
    void processCsvFile(String filename, String separator) throws IOException;
}
