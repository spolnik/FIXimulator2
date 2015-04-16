package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.IndicationsOfInterestApi;
import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.fix.FIXimulator;
import org.nprogramming.fiximulator2.domain.IOI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class IndicationsOfInterestRepository implements IndicationsOfInterestApi {

    private final List<IOI> iois = new ArrayList<>();
    private Callback callback = null;

    private static final Logger LOG = LoggerFactory.getLogger(IndicationsOfInterestRepository.class);

    @Override
    public void addIndicationOfInterest(IOI ioi) {
        iois.add(ioi);
        int limit = 50;
        try {
            limit = (int) FIXimulator.getApplication().getSettings()
                    .getLong("FIXimulatorCachedObjects");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        while (iois.size() > limit) {
            iois.remove(0);
        }
        callback.update();
    }

    @Override
    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int size() {
        return iois.size();
    }

    @Override
    public IOI getIOI(int i) {
        return iois.get(i);
    }

    @Override
    public IOI getIOI(String id) {
        for (IOI ioi : iois) {
            if (ioi.getID().equals(id))
                return ioi;
        }
        return null;
    }
}
