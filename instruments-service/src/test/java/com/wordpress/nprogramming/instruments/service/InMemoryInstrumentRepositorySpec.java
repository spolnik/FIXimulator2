package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.Instrument;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InMemoryInstrumentRepositorySpec {

    private InMemoryInstrumentRepository repository;

    @Before
    public void setUp() throws Exception {
        InstrumentsConfiguration configuration = mock(InstrumentsConfiguration.class);
        when(configuration.getDefaultInputFilePath()).thenReturn("instruments.json");
        repository = new InMemoryInstrumentRepository(configuration);
    }

    @Test
    public void allowsToQueryParticularInstrument() throws Exception {
        Instrument instrument = repository.getInstrument("IBM");

        assertThat(instrument).isNotNull();
    }

    @Test
    public void allowsToQueryAllInstruments() throws Exception {
        List<Instrument> instruments = repository.getAll();

        assertThat(instruments.size()).isGreaterThan(10);
    }

    @Test
    public void returnsEmptyInstrumentIfQueriedForNonExistingIdentifier() throws Exception {
        Instrument emptyInstrument = repository.getInstrument("dummmy");

        assertThat(emptyInstrument.getName()).isEmpty();
        assertThat(emptyInstrument.getCusip()).isEmpty();
        assertThat(emptyInstrument.getPrice()).isEmpty();
        assertThat(emptyInstrument.getRIC()).isEmpty();
        assertThat(emptyInstrument.getSedol()).isEmpty();
        assertThat(emptyInstrument.getTicker()).isEmpty();
    }
}