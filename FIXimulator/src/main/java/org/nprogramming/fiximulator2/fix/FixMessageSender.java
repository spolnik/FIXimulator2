package org.nprogramming.fiximulator2.fix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.OnBehalfOfCompID;
import quickfix.field.OnBehalfOfSubID;

final class FixMessageSender {

    private static final Logger LOG = LoggerFactory.getLogger(FixMessageSender.class);

    private final SessionSettings settings;
    private final SessionID currentSession;

    public FixMessageSender(SessionSettings settings, SessionID currentSession) {
        this.settings = settings;
        this.currentSession = currentSession;
    }

    public void send(Message message) {
        String oboCompID = "<UNKNOWN>";
        String oboSubID = "<UNKNOWN>";
        boolean sendoboCompID = false;
        boolean sendoboSubID = false;

        try {
            oboCompID = settings.getString(currentSession, "OnBehalfOfCompID");
            oboSubID = settings.getString(currentSession, "OnBehalfOfSubID");
            sendoboCompID = settings.getBool("FIXimulatorSendOnBehalfOfCompID");
            sendoboSubID = settings.getBool("FIXimulatorSendOnBehalfOfSubID");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }

        // Add OnBehalfOfCompID
        if (sendoboCompID && !oboCompID.equals("")) {
            OnBehalfOfCompID onBehalfOfCompID = new OnBehalfOfCompID(oboCompID);
            quickfix.fix42.Message.Header header = (quickfix.fix42.Message.Header) message.getHeader();
            header.set(onBehalfOfCompID);
        }

        // Add OnBehalfOfSubID
        if (sendoboSubID && !oboSubID.equals("")) {
            OnBehalfOfSubID onBehalfOfSubID = new OnBehalfOfSubID(oboSubID);
            quickfix.fix42.Message.Header header = (quickfix.fix42.Message.Header) message.getHeader();
            header.set(onBehalfOfSubID);
        }

        // Send actual message
        try {
            Session.sendToTarget(message, currentSession);
        } catch (SessionNotFound e) {
            LOG.error("Error: ", e);
        }
    }
}
