package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Instrument;

import java.io.File;

public interface InstrumentsApi {

    Instrument getInstrument(String symbol);

    Instrument randomInstrument();

    void addCallback(Callback callback);

    int size();

    Instrument getInstrument(int row);

    void reloadInstrumentSet(File file);
}
