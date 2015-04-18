package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Instrument;

public interface InstrumentsApi {

    Instrument getInstrument(String symbol);

    Instrument randomInstrument();

    int size();

    Instrument getInstrument(int row);
}
