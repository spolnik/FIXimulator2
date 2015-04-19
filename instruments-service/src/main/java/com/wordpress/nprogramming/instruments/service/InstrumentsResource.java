package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/instruments")
@Produces(MediaType.APPLICATION_JSON)
public class InstrumentsResource {

    private final InstrumentsApi instrumentsApi;

    public InstrumentsResource(InstrumentsApi instrumentsApi) {

        this.instrumentsApi = instrumentsApi;
    }

    @GET
    public List<Instrument> listInstruments() {
        return instrumentsApi.getAll();
    }
}
