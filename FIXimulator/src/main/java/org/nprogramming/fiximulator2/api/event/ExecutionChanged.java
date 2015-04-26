package org.nprogramming.fiximulator2.api.event;

public final class ExecutionChanged {
    private final String id;

    public ExecutionChanged(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static ExecutionChanged withId(String id) {
        return new ExecutionChanged(id);
    }
}
