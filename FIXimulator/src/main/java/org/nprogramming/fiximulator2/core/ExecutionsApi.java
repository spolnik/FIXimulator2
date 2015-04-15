package org.nprogramming.fiximulator2.core;

import org.nprogramming.fiximulator2.ui.ExecutionTableModel;

public final class ExecutionsApi {
    private ExecutionSet executions = new ExecutionSet();

    public Execution getExecution(String id) {
        return executions.getExecution(id);
    }

    public void update() {
        executions.update();
    }

    public void addExecution(Execution execution) {
        executions.add(execution);
    }

    public Execution getExecution(int id) {
        return executions.getExecution(id);
    }

    public void addCallback(ExecutionTableModel executionTableModel) {
        executions.addCallback(executionTableModel);
    }

    public int getCount() {
        return executions.getCount();
    }
}
