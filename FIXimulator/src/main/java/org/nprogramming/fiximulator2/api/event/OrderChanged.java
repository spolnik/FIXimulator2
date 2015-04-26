package org.nprogramming.fiximulator2.api.event;

public final class OrderChanged {
    private final String id;

    public OrderChanged(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static OrderChanged withId(String id) {
        return new OrderChanged(id);
    }
}
