package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.NotifyService;

import java.util.HashMap;
import java.util.Map;

public class InMemoryNotificationService implements NotifyService {

    private final Map<Class, MessageHandler> messageHandlers = new HashMap<>();

    @Override
    public <TMessage> void register(Class<TMessage> clazz, MessageHandler<TMessage> messageHandler) {
        messageHandlers.put(clazz, messageHandler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TMessage> void send(Class<TMessage> clazz, TMessage message) {
        MessageHandler<TMessage> messageHandler =
                (MessageHandler<TMessage>)messageHandlers.get(clazz);
        messageHandler.onMessage(message);
    }
}
