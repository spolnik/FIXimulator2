package com.wordpress.nprogramming.instruments.api;

import java.util.List;

public interface InstrumentsApi {

    Instrument getInstrument(String symbol);

    List<Instrument> getAll();
}
