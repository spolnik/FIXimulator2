package org.nprogramming.fiximulator2.ui;

import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.event.ConnectionStatus;

import javax.swing.*;

public final class LabelStatusSwitcher implements MessageHandler<ConnectionStatus> {

    private final JLabel label;

    private final ImageIcon green = new ImageIcon(getClass()
            .getResource("/img/green.gif"));

    private final ImageIcon red = new ImageIcon(getClass()
        .getResource("/img/red.gif"));

    public LabelStatusSwitcher(JLabel label) {

        this.label = label;
    }

    @Override
    public void onMessage(ConnectionStatus message) {
        label.setIcon(message.isConnected() ? green : red);
    }
}
