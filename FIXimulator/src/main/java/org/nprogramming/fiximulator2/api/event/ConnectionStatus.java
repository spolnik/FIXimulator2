package org.nprogramming.fiximulator2.api.event;

public class ConnectionStatus {
    private final boolean connected;

    public ConnectionStatus(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public static ConnectionStatus on() {
        return new ConnectionStatus(true);
    }

    public static ConnectionStatus off() {
        return new ConnectionStatus(false);
    }
}
