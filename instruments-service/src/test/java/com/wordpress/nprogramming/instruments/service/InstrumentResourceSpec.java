package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.InstrumentsRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InstrumentResourceSpec {

    private InstrumentsRepository instrumentsRepository;
    private InstrumentResource resource;

    @Before
    public void setUp() throws Exception {
        instrumentsRepository = mock(InstrumentsRepository.class);
        resource = new InstrumentResource(instrumentsRepository);
    }

    @Test
    public void delegatesQueryForAllInstrumentsToApi() throws Exception {
        resource.listInstruments();

        verify(instrumentsRepository, atLeastOnce()).getAll();
    }

    @Test
    public void delegatesQueryForParticularInstrumentToApi() throws Exception {
        resource.instrument("IBM");

        verify(instrumentsRepository, atLeastOnce()).queryById("IBM");
    }
}