package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Instrument;

import java.util.List;

public interface InstrumentsApi {

    Instrument getInstrument(String symbol);

    List<Instrument> getAll();
}
