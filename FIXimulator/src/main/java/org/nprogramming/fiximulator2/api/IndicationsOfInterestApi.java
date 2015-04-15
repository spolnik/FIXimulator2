package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.IOI;

public interface IndicationsOfInterestApi {

    void addIndicationOfInterest(IOI ioi);

    IOI getIOI(int id);

    void addCallback(NotifyApi notifyApi);

    int size();

    IOI getIOI(String id);
}
