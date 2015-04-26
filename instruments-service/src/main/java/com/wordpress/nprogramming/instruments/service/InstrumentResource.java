package com.wordpress.nprogramming.instruments.service;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/instruments")
@Produces(MediaType.APPLICATION_JSON)
public class InstrumentResource {

    private final InstrumentsRepository instrumentsRepository;

    public InstrumentResource(InstrumentsRepository instrumentsRepository) {
        this.instrumentsRepository = instrumentsRepository;
    }

    @GET
    public List<Instrument> listInstruments() {
        return instrumentsRepository.getAll();
    }

    @GET
    @Path("{instrumentId}")
    public Instrument instrument(@PathParam("instrumentId") String instrumentId) {
        return instrumentsRepository.queryById(instrumentId);
    }
}
