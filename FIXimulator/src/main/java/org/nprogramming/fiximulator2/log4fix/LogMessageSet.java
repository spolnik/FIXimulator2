package org.nprogramming.fiximulator2.log4fix;

import org.nprogramming.fiximulator2.api.Callback;
import quickfix.DataDictionary;
import quickfix.Message;
import quickfix.SessionID;

import java.util.ArrayList;
import java.util.List;


public class LogMessageSet {

    private List<LogMessage> messages = null;
    private Callback callback;
    private int messageIndex = 0;

    public LogMessageSet() {
        messages = new ArrayList<>();
    }

    public void add(Message message, boolean incoming,
                    DataDictionary dictionary, SessionID sessionID) {
        messageIndex++;
        LogMessage msg =
                new LogMessage(messageIndex, incoming, sessionID,
                        message.toString(), dictionary);
        messages.add(msg);


        callback.update();
    }

    public LogMessage getMessage(int i) {
        return messages.get(i);
    }

    public int size() {
        return messages.size();
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }
}
