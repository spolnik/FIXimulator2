package com.wordpress.nprogramming.instruments.service;

import com.codahale.metrics.health.HealthCheck;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class InstrumentsFileHealthCheck extends HealthCheck {

    private final String inputFilePath;

    public InstrumentsFileHealthCheck(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    @Override
    protected Result check() throws Exception {

        URL instrumentsFile = readInstruments();

        if (doesNotExist(instrumentsFile)) {
            return Result.unhealthy("File does not exist: " + inputFilePath);
        }

        return Result.healthy();
    }

    private URL readInstruments() {
        return InstrumentsFileHealthCheck.class.getClassLoader().getResource(
                inputFilePath
        );
    }

    private boolean doesNotExist(URL resource) throws URISyntaxException {

        return resource == null ||
                !Paths.get(resource.toURI()).toFile().exists();
    }
}
