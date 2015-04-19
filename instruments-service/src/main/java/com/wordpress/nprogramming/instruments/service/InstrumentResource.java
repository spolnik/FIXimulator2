package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/instruments")
@Produces(MediaType.APPLICATION_JSON)
public class InstrumentResource {

    private final InstrumentsApi instrumentsApi;

    public InstrumentResource(InstrumentsApi instrumentsApi) {
        this.instrumentsApi = instrumentsApi;
    }

    @GET
    public List<Instrument> listInstruments() {
        return instrumentsApi.getAll();
    }

    @GET
    @Path("{instrumentId}")
    public Instrument getInstrument(@PathParam("instrumentId") String instrumentId) {
        return instrumentsApi.getInstrument(instrumentId);
    }
}
