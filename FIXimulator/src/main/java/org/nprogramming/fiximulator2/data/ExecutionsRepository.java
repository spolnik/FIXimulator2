package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.ExecutionsApi;
import org.nprogramming.fiximulator2.api.NotifyApi;
import org.nprogramming.fiximulator2.domain.Execution;

import java.util.*;

public final class ExecutionsRepository implements ExecutionsApi {

    private final Map<String, Execution> executions = new HashMap<>();
    private NotifyApi callback = null;

    @Override
    public void addExecution(Execution execution) {

        executions.put(execution.getID(), execution);
        callback.added(execution.getID());
    }

    @Override
    public void update(String id) {
        callback.update(id);
    }

    @Override
    public void addCallback(NotifyApi callback) {
        this.callback = callback;
    }

    @Override
    public int size() {
        return executions.size();
    }

    @Override
    public List<Execution> getAll() {
        return Collections.unmodifiableList(
                new ArrayList<>(executions.values())
        );
    }

    @Override
    public Execution getExecution(String id) {
        if (executions.containsKey(id)) {
            return executions.get(id);
        }

        return null;
    }
}
