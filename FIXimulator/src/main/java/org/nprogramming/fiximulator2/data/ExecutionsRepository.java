package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.ExecutionsApi;
import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.fix.FIXimulator;
import org.nprogramming.fiximulator2.domain.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionsRepository implements ExecutionsApi {

    private final List<Execution> executions = new ArrayList<>();
    private Callback callback = null;

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionsRepository.class);

    @Override
    public void addExecution ( Execution execution ) {
        executions.add( execution );
        int limit = 50;
        try {
            limit = (int) FIXimulator.getApplication().getSettings()
                    .getLong("FIXimulatorCachedObjects");
        } catch ( Exception e ) {
            LOG.error("Error: ", e);
        }
        while ( executions.size() > limit ) {
            executions.remove(0);
        }
        callback.update();
    }

    @Override
    public void update () {
        callback.update();
    }

    @Override
    public void addCallback(Callback callback){
        this.callback = callback;
    }

    @Override
    public int size() {
        return executions.size();
    }

    @Override
    public Execution getExecution( int i ) {
        return executions.get( i );
    }

    @Override
    public Execution getExecution( String id ) {
        for (Execution execution : executions) {
            if (execution.getID().equals(id))
                return execution;
        }
        return null;
    }
}
