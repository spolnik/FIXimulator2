package com.wordpress.nprogramming.instruments.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InMemoryInstrumentRepository implements InstrumentsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(
            InMemoryInstrumentRepository.class
    );

    private final List<Instrument> instruments = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InMemoryInstrumentRepository(InstrumentsConfiguration configuration) {

        URL instrumentsJson =
                readInstruments(configuration);

        Preconditions.checkNotNull(
                instrumentsJson,
                configuration.getDefaultInputFilePath() + " does not exist."
        );

        loadInstruments(instrumentsJson);
    }

    private URL readInstruments(InstrumentsConfiguration configuration) {

        return InMemoryInstrumentRepository.class.getClassLoader().getResource(
                configuration.getDefaultInputFilePath()
        );
    }

    private void loadInstruments(URL instrumentsJson) {
        try {
            File file = new File(
                    instrumentsJson.getPath()
            );

            instruments(file).forEach(
                    this.instruments::add
            );
        } catch (Exception e) {
            LOG.error("Error reading/parsing instrument file.", e);
        }
    }

    private List<Instrument> instruments(File file) throws IOException {
        return objectMapper.readValue(
                file, new TypeReference<List<Instrument>>() {
                });
    }

    @Override
    public Instrument queryById(String identifier) {

        return instruments.stream()
                .filter(x -> x.canBeIdentifiedBy(identifier))
                .findFirst()
                .orElse(Instrument.empty());
    }

    @Override
    public List<Instrument> getAll() {
        return Collections.unmodifiableList(instruments);
    }
}

