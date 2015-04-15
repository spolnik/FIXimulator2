package org.nprogramming.fiximulator2.core;

import org.nprogramming.fiximulator2.ui.IOITableModel;

public final class IndicationsOfInterestApi {
    private final IOIset iois = new IOIset();

    public void addIndicationOfInterest(IOI ioi) {
        iois.add(ioi);
    }

    public IOI getIOI(int id) {
        return iois.getIOI(id);
    }

    public void addCallback(IOITableModel ioiTableModel) {
        iois.addCallback(ioiTableModel);
    }

    public int getCount() {
        return iois.getCount();
    }
}
