package com.wordpress.nprogramming.instruments.api;

import java.util.List;

public interface InstrumentsRepository {

    Instrument queryById(String symbol);

    List<Instrument> getAll();
}
