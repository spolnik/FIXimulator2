package org.nprogramming.fiximulator2.api.event;

public final class IOIChanged {
    private final String id;

    public IOIChanged(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static IOIChanged withId(String id) {
        return new IOIChanged(id);
    }
}
