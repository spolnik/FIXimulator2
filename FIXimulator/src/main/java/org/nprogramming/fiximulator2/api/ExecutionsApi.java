package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Execution;

import java.util.List;

public interface ExecutionsApi {

    Execution getExecution(String id);

    void update(String id);

    void addExecution(Execution execution);

    void addCallback(NotifyApi callback);

    int size();

    List<Execution> getAll();
}
