package com.wordpress.nprogramming.instruments.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InstrumentRepository implements InstrumentsApi {

    private static final Logger LOG = LoggerFactory.getLogger(InstrumentRepository.class);

    private final List<Instrument> instruments = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InstrumentRepository(InstrumentsConfiguration configuration) {

        URL instrumentsJson =
                InstrumentRepository.class.getClassLoader().getResource(
                        configuration.getDefaultInputFilePath()
                );

        Preconditions.checkNotNull(
                instrumentsJson,
                configuration.getDefaultInputFilePath() + " does not exist."
        );

        loadInstruments(instrumentsJson);
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

    private List<Instrument> instruments(File file) throws java.io.IOException {
        return objectMapper.readValue(
                file, new TypeReference<List<Instrument>>() {
                });
    }

    @Override
    public Instrument getInstrument(String identifier) {

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

