package com.wordpress.nprogramming.instruments.client;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Stream;

public class InstrumentsHttpClient implements InstrumentsApi {

    private final Client client;

    public InstrumentsHttpClient() {
        client = ClientBuilder.newClient();
    }

    @Override
    public Instrument getInstrument(String symbol) {

        WebTarget target = client.target("http://localhost:8080")
                .path("api/instruments/" + symbol);

        return target.request(MediaType.APPLICATION_JSON).get(Instrument.class);
    }

    @Override
    public List<Instrument> getAll() {
        WebTarget target = client.target("http://localhost:8080")
                .path("api/instruments");

        return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<Instrument>>(){});
    }

    public static void main(String[] args) {
        InstrumentsHttpClient httpClient = new InstrumentsHttpClient();

        Instrument instrument = httpClient.getInstrument("IBM");

        System.out.println(instrument);

        Stream<Instrument> instruments = httpClient.getAll().stream().limit(10);
        instruments.forEach(System.out::println);
    }
}
