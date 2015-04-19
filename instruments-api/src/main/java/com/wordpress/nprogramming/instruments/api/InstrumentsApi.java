package com.wordpress.nprogramming.instruments.api;


import com.wordpress.nprogramming.instruments.api.Instrument;

import java.util.List;

public interface InstrumentsApi {

    Instrument getInstrument(String symbol);

    List<Instrument> getAll();
}
