package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.NotifyService;

public class InMemoryNotificationService implements NotifyService {

    private MessageHandler executionMessageHandler = null;
    private MessageHandler orderMessageHandler = null;
    private MessageHandler ioiMessageHandler = null;

    @Override
    public void addExecutionMessageHandler(MessageHandler messageHandler) {
        executionMessageHandler = messageHandler;
    }

    @Override
    public void addOrderMessageHandler(MessageHandler messageHandler) {
        orderMessageHandler = messageHandler;
    }

    @Override
    public void addIOIMessageHandler(MessageHandler messageHandler) {
        ioiMessageHandler = messageHandler;
    }

    @Override
    public void sendChangedExecutionId(String id) {
        executionMessageHandler.onMessage(id);
    }

    @Override
    public void sendChangedOrderId(String id) {
        orderMessageHandler.onMessage(id);
    }

    @Override
    public void sendChangedIOIId(String id) {
        ioiMessageHandler.onMessage(id);
    }
}
