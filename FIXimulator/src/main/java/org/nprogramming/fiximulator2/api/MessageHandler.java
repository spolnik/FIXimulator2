package org.nprogramming.fiximulator2.api;

public interface MessageHandler<TMessage> {

    void onMessage(TMessage id);
}
