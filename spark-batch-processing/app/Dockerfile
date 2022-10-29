FROM openjdk:8
COPY ./target /tmp
WORKDIR /tmp
CMD ["java", "-cp", "sparkbatchprocessing-0.0.1.jar", "com.jdfs.assessment.wheatheranalysis.App"]