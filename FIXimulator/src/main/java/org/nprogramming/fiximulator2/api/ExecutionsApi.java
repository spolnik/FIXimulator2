package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Execution;

public interface ExecutionsApi {

    Execution getExecution(String id);

    void update();

    void addExecution(Execution execution);

    Execution getExecution(int id);

    void addCallback(Callback callback);

    int size();
}
