package org.nprogramming.fiximulator2.api;

public interface NotifyService {

    <TMessage> void register(Class<TMessage> clazz, MessageHandler<TMessage> message);

    <TMessage> void send(Class<TMessage> clazz, TMessage tMessage);
}
