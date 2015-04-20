package org.nprogramming.fiximulator2.api;

public interface NotifyService {

    void addExecutionMessageHandler(MessageHandler messageHandler);

    void addOrderMessageHandler(MessageHandler messageHandler);

    void addIOIMessageHandler(MessageHandler messageHandler);

    void sendChangedExecutionId(String id);

    void sendChangedOrderId(String id);

    void sendChangedIOIId(String id);
}
