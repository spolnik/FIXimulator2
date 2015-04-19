package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.InstrumentsApi;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InstrumentResourceSpec {

    private InstrumentsApi instrumentsApi;
    private InstrumentResource resource;

    @Before
    public void setUp() throws Exception {
        instrumentsApi = mock(InstrumentsApi.class);
        resource = new InstrumentResource(instrumentsApi);
    }

    @Test
    public void delegatesQueryForAllInstrumentsToApi() throws Exception {
        resource.listInstruments();

        verify(instrumentsApi, atLeastOnce()).getAll();
    }

    @Test
    public void delegatesQueryForParticularInstrumentToApi() throws Exception {
        resource.getInstrument("IBM");

        verify(instrumentsApi, atLeastOnce()).getInstrument("IBM");
    }
}